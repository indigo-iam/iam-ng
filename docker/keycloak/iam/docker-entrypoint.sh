#!/bin/bash

# Setup IAM realms

if [ -z "${IAM_KC_SKIP_SETUP}" ]; then
    /bin/bash /opt/jboss/tools/docker-entrypoint.sh $@ >/tmp/setup-log.out 2>&1 &
    /bin/bash /opt/jboss/keycloak/iam/wait-for-it.sh -h localhost -p 8080 -t 120
    /bin/bash /opt/jboss/keycloak/iam/setup-iam-realms.sh

    JBOSS_PID=$(jps | grep jboss | cut -d ' ' -f1)
    kill ${JBOSS_PID}
    sleep 5
fi

exec /opt/jboss/tools/docker-entrypoint.sh $@
exit $?
