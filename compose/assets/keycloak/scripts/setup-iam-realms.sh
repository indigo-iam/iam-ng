#/bin/bash
set -ex

UUID_GREP_EXP='[a-fA-F0-9]\{8\}-[a-fA-F0-9]\{4\}-[a-fA-F0-9]\{4\}-[a-fA-F0-9]\{4\}-[a-fA-F0-9]\{12\}'

MAIL_HOST=${MAIL_HOST:-mailhog}
MAIL_HOST_PORT=${MAIL_HOST_PORT:-1025}

JBOSS_HOME=/opt/jboss/keycloak
KC_CLI=${JBOSS_HOME}/bin/kcadm.sh

KEYCLOAK_USER=${KEYCLOAK_USER:-admin}
KEYCLOAK_PASSWORD=${KEYCLOAK_PASSWORD:-password}

IAM_API_CLIENT_NAME=${IAM_API_CLIENT_NAME:-iam-api}
IAM_API_CLIENT_SECRET=${IAM_API_CLIENT_SECRET:-secret}

IAM_CLI_CLIENT_NAME=${IAM_CLI_CLIENT_NAME:-iam-cli}

IAM_DASHBOARD_CLIENT_NAME=${IAM_DASHBOARD_CLIENT_NAME:-iam-dashboard}

IAM_ADMIN_USER=${IAM_ADMIN_USER:-admin}
IAM_ADMIN_PASSWORD=${IAM_ADMIN_PASSWORD:-password}

IAM_REALM_NAMES=${IAM_REALM_NAMES:-iam alice atlas cms lhcb}

IAM_KC_SETUP_MARKER=${IAM_KC_SETUP_MARKER:-/iam/iam-setup-done}

${KC_CLI} config credentials \
  --server http://localhost:8080/auth \
  --realm master \
  --user ${KEYCLOAK_USER} \
  --password ${KEYCLOAK_PASSWORD}


for realm in ${IAM_REALM_NAMES}; do
  echo "Configuring realm ${realm}"

  ${KC_CLI} create realms -s realm=${realm} -s enabled=true

  # Set mailhog SMTP server
  ${KC_CLI} update realms/${realm} -s "smtpServer.host=${MAIL_HOST}" \
    -s "smtpServer.port=${MAIL_HOST_PORT}" \
    -s "smtpServer.from=${realm}@kc.local.io" \
    -s "smtpServer.auth=false" \
    -s "smtpServer.ssl=false"

  ${KC_CLI} create roles -r ${realm} -s name=iam-user
  ${KC_CLI} create roles -r ${realm} -s name=iam-admin 
  ${KC_CLI} create roles -r ${realm} -s name=iam-owner

  ${KC_CLI} add-roles -r ${realm} --rname iam-admin --rolename iam-user --rolename iam-owner
  ${KC_CLI} add-roles -r ${realm} --rname iam-owner --rolename iam-user

  iam_cs_id=$(${KC_CLI} create client-scopes -r ${realm} -s name="iam" -s protocol="openid-connect" -s 'attributes."include.in.token.scope"=false' -s 'attributes."display.on.consent.screen"=false'|& grep -o ${UUID_GREP_EXP})

  ${KC_CLI} create clients -r ${realm} \
    -s clientId=${IAM_CLI_CLIENT_NAME} \
    -s enabled=true \
    -s publicClient=true \
    -s standardFlowEnabled=false \
    -s directAccessGrantsEnabled=true \
    -s description="The IAM API cli"

  ${KC_CLI} create clients -r ${realm} \
    -s clientId=${IAM_API_CLIENT_NAME} \
    -s enabled=true \
    -s secret=${IAM_API_CLIENT_SECRET} \
    -s clientAuthenticatorType=client-secret \
    -s serviceAccountsEnabled=true \
    -s standardFlowEnabled=false \
    -s description="The IAM API server client registration"

  ## Add management roles for the realm
  ${KC_CLI} add-roles -r ${realm} \
    --uusername service-account-${IAM_API_CLIENT_NAME} \
    --cclientid realm-management \
    --rolename manage-users \
    --rolename manage-clients 

  ${KC_CLI} create clients -r ${realm} \
    -s clientId=${IAM_DASHBOARD_CLIENT_NAME} \
    -s enabled=true \
    -s publicClient=true \
    -s 'redirectUris=["'${IAM_DASHBOARD_BASE_URL}'/*"]'  \
    -s description="The IAM API server dashboard"

  admin_id=$(${KC_CLI} create users -r ${realm} -s username=${IAM_ADMIN_USER} -i)

  ${KC_CLI} add-roles -r ${realm} \
    --uusername ${IAM_ADMIN_USER} \
    --cclientid realm-management \
    --rolename realm-admin

  ${KC_CLI} add-roles -r ${realm} \
    --uusername ${IAM_ADMIN_USER} \
    --rolename iam-admin \
    --rolename iam-owner

  ${KC_CLI} update users/${admin_id} \
    -r ${realm} \
    -s enabled=true

  ${KC_CLI} set-password -r ${realm} \
    --username ${IAM_ADMIN_USER} \
    --new-password ${IAM_ADMIN_PASSWORD}

  echo "Realm ${realm} setup completed."
done

touch ${IAM_KC_SETUP_MARKER}
echo "Setup completed"
