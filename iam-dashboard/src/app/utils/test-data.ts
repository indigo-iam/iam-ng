export const SINGLE_CLIENT = {
  id: 'c476342d-3588-4877-b560-cd4c37b0fe65',
  clientId: 'account',
  name: '${client_account}',
  rootUrl: '${authBaseUrl}',
  baseUrl: '/realms/alice/account/',
  surrogateAuthRequired: false,
  enabled: true,
  alwaysDisplayInConsole: false,
  clientAuthenticatorType: 'client-secret',
  defaultRoles: [
    'view-profile',
    'manage-account'
  ],
  redirectUris: [
    '/realms/alice/account/*'
  ],
  webOrigins: [],
  notBefore: 0,
  bearerOnly: false,
  consentRequired: false,
  standardFlowEnabled: true,
  implicitFlowEnabled: false,
  directAccessGrantsEnabled: false,
  serviceAccountsEnabled: false,
  publicClient: false,
  frontchannelLogout: false,
  protocol: 'openid-connect',
  attributes: {},
  authenticationFlowBindingOverrides: {},
  fullScopeAllowed: false,
  nodeReRegistrationTimeout: 0,
  defaultClientScopes: [
    'web-origins',
    'role_list',
    'profile',
    'roles',
    'email'
  ],
  optionalClientScopes: [
    'address',
    'phone',
    'offline_access',
    'microprofile-jwt'
  ],
  access: {
    view: true,
    configure: true,
    manage: true
  }
}

export const CLIENT_NO_ORIGIN_OR_REDIRECT = {
  id: 'c476342d-3588-4877-b560-cd4c37b0fe65',
  clientId: 'account',
  name: '${client_account}',
  rootUrl: '${authBaseUrl}',
  baseUrl: '/realms/alice/account/',
  surrogateAuthRequired: false,
  enabled: true,
  alwaysDisplayInConsole: false,
  clientAuthenticatorType: 'client-secret',
  defaultRoles: [
    'view-profile',
    'manage-account'
  ],
  notBefore: 0,
  bearerOnly: false,
  consentRequired: false,
  standardFlowEnabled: true,
  implicitFlowEnabled: false,
  directAccessGrantsEnabled: false,
  serviceAccountsEnabled: false,
  publicClient: false,
  frontchannelLogout: false,
  protocol: 'openid-connect',
  attributes: {},
  authenticationFlowBindingOverrides: {},
  fullScopeAllowed: false,
  nodeReRegistrationTimeout: 0,
  defaultClientScopes: [
    'web-origins',
    'role_list',
    'profile',
    'roles',
    'email'
  ],
  optionalClientScopes: [
    'address',
    'phone',
    'offline_access',
    'microprofile-jwt'
  ],
  access: {
    view: true,
    configure: true,
    manage: true
  },
  adminUrl: 'adminurl',
  description: 'description'
}

export const SINGLE_CLIENT_LIST = [
  SINGLE_CLIENT
]

export const SAML_CLIENT =
{
  id: '7d48ec06-2ebe-41ed-8f33-8f8e2ae3096c',
  clientId: 'https://sso.example.com/portal',
  surrogateAuthRequired: false,
  enabled: true,
  alwaysDisplayInConsole: false,
  clientAuthenticatorType: 'client-secret',
  redirectUris: [
    'https://service.example.com/SAML2/SSO/POST'
  ],
  webOrigins: [
    'https://service.example.com'
  ],
  notBefore: 0,
  bearerOnly: false,
  consentRequired: false,
  standardFlowEnabled: true,
  implicitFlowEnabled: false,
  directAccessGrantsEnabled: false,
  serviceAccountsEnabled: false,
  publicClient: false,
  frontchannelLogout: true,
  protocol: 'saml',
  attributes: {
    'saml.assertion.signature': 'true',
    'saml.force.post.binding': 'true',
    'saml.encrypt': 'true',
    saml_assertion_consumer_url_post: 'https://service.example.com/SAML2/SSO/POST',
    'saml.server.signature': 'true',
    'saml.server.signature.keyinfo.ext': 'false',
    'saml.signing.certificate': 'MIICyzCCAbMCBgFxyrlIVzANBgkqhkiG9w0BAQsFADApMScwJQYDVQQDDB5odHRwczovL3Nzby5leGFtcGxlLmNvbS9wb3J0YWwwHhcNMjAwNDMwMTA1MjUyWhcNMzAwNDMwMTA1NDMyWjApMScwJQYDVQQDDB5odHRwczovL3Nzby5leGFtcGxlLmNvbS9wb3J0YWwwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCKxBZwiIZ2lzpRGYaWz4TP2XwhqqdzPlMCirIKnEFN6QC1ag+TxaMJ8iBf6KuQ5S2bVa7agzQ+K2je0PPUxtACafJHcFwVVhEenmHJErPSvPhtNcmkbyf/jT4B566pUOHe2D0XIfxue0Md+zYTfdAZTu7ecExVaE6WYb8QiMvjYzTLG9FWzg51Ay/d+Fb/dTAF9q1yVJVC7ndYShpXvC89JAvO6uJ1RWiWYnfF81jAZ4pYRlVbeLP9mB8fEMbKim+hnO7KSPoJbK1On9dHYmPFVD9GwRuuEDtGaWAszhfPSG/7EcV66JCEcRcUE4GjiRkpXanVqgyCwZSVRdiQCvpFAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAAMWGyeLbXq/T27lz7ULEY3V1KRnZNtXKlkb65nf7aHIpxVbNcisAwZ2JKIImTg7VPtGRhsT/efL4PO9omeaN6O7o4ILAEnC9hhf3kndvf+SWTuvBoeH63rPR7HR8nins5AliksjdzuJdB/ktHtmXlFmbs5hup9zXLPtxmFGM0n9r/07paVw5/Z0PAPw+kjLTEIrZaNkFGoYnYi7JuLaG/AD+B6B9ZvA7A4iyfv5RbxdPNiQVgY5KojvHEChPsiwO5bQjzzZjYfiRrtNkfLLfBW5SVncgSFYm08wvyaatZEJNG6Y7GrgcImvO9+wTn+HVCCgqssu2N5pUgGzU0Ch3CM=',
    'saml.signature.algorithm': 'RSA_SHA256',
    saml_force_name_id_format: 'false',
    'saml.client.signature': 'true',
    'saml.authnstatement': 'true',
    'saml.signing.private.key': 'MIIEowIBAAKCAQEAisQWcIiGdpc6URmGls+Ez9l8Iaqncz5TAoqyCpxBTekAtWoPk8WjCfIgX+irkOUtm1Wu2oM0Pito3tDz1MbQAmnyR3BcFVYRHp5hyRKz0rz4bTXJpG8n/40+AeeuqVDh3tg9FyH8bntDHfs2E33QGU7u3nBMVWhOlmG/EIjL42M0yxvRVs4OdQMv3fhW/3UwBfatclSVQu53WEoaV7wvPSQLzuridUVolmJ3xfNYwGeKWEZVW3iz/ZgfHxDGyopvoZzuykj6CWytTp/XR2JjxVQ/RsEbrhA7RmlgLM4Xz0hv+xHFeuiQhHEXFBOBo4kZKV2p1aoMgsGUlUXYkAr6RQIDAQABAoIBAB/vryZpFWE3vtEpacNNsUhQynPJHc61Gk+scftlKNZweJxH85vT0YkZc3//QXUQeaWVW6+ooJ5qSTF2r5dZxke38G2mm1rBuyDr3J91MXOf0CD6TxUMDA2RiSLRGm6XUdSCudSYGsumjLTauwBTZVBdzTXebIL/hY6wolOA2EPVmy+KfwPOF3eP71lkYmvQSbfEGse4hILumZw4K2szwDmMouwsrG0tJPvftbiU3TdN3KpvvjxeiztMFWUvZZMj4guXvhtHQFlbpFF7ACxZJtN349rhRJEAZPMCHZaFgavxYRZNZKMD59TOhMaK8TmcB3Ga3b9zUDBkkCGy4pxZRaUCgYEAy3AJDp4zzTJDVOYm959yPUgt48qJp5yLj/XzWzHrW0GCmoluVB8Bq40Ld18iMxDXBWNIqUjihNqu4UCHey+LWjiQg7i0701q2HPBidICHNI388+TWCmUUgGwzbw6HNrkeg3LcLsc6QqUi1RrezTM62BpynKh2Y1Mz4fhatU63YcCgYEArp56O+sclYT0DE1hy/9OQ6ZHs1+I3c6GZtGQpZjwY99w9dTydJvcM7FTCW0am9SL/PdcMu+pFOHZZPP1npfU65B/LRV1f09UrlMhwhbdxU0fMQ9SBuUPRPt7gswK57MuKBHcHdo2gBH3Id8g7QyGw8MtN7DRLfTzfxG4Vi8/fNMCgYEAywmGAJDkImeHy7PoMDtySLdBFZeOyznyEDvoJZwsyWcYciO3eT147T5Xs0u/Vjf4Yeg1HL8hVHbtEuLUVJ2e9ux9TvmJm0wo1t6wylxOv3UPYsZIyDS3UyFc0/iIB0Rvw2EBKgGVWQC2QFiA7PZFjqUq8GJ4JihgAMAcugMWPQECgYADBNBeKtFisFq7v7a6HR7QQpaovjUSiJiAPI1An6VARMOiLF1IsM0cw6sI1a8JFKKa6/fXFZeZudHVUfZkGIHg1K2uLNnMKqXvIZbK0TRiTdQFOBuV7O3XpnVbLIXfvZRFsxFYHsakbisUldPu5TGLPtuQQ+lJ3xs6jiberpBtawKBgGFYoqycDrlSlLY3qDIPz5CaKT/EULfw8TNRyaavIuU9Sy9vrGxcpwqD3OZZJsQkWebrBKe0J5Pb9nPAZNYLIRyUo0WWn7UEUCjkUUXiTOP8DdaW+puxdxmOoFnCuDGmtp7LPVEBy20hyHvEyv4bU4zbznxaEhjLrSKERyorB073',
    saml_name_id_format: 'transient',
    saml_signature_canonicalization_method: 'http://www.w3.org/2001/10/xml-exc-c14n#'
  },
  authenticationFlowBindingOverrides: {},
  fullScopeAllowed: true,
  nodeReRegistrationTimeout: -1,
  defaultClientScopes: [
    'web-origins',
    'role_list',
    'profile',
    'roles',
    'email'
  ],
  optionalClientScopes: [
    'address',
    'phone',
    'offline_access',
    'microprofile-jwt'
  ],
  access: {
    view: true,
    configure: true,
    manage: true
  }
}

export const SAML_CLIENT_LIST = [
  SAML_CLIENT
]

export const REGISTRATION_REQUESTS = {
  totalResults: 1,
  itemsPerPage: 10,
  startIndex: 0,
  resources: [
    {
      kind: 'RegistrationRequest',
      id: '3',
      uuid: '6044c90e-1ce1-477b-b6bf-cdd7d1d3b023',
      metadata: {
        creationTime: '2020-05-20T12:58:54Z',
        lastUpdateTime: '2020-05-20T12:58:54Z'
      },
      realm: {
        name: 'alice'
      },
      requesterInfo: {
        username: 'test3',
        givenName: 'test',
        familyName: 'test',
        email: 'test@example.com',
        emailVerified: false
      },
      otherInfo: {},
      labels: [],
      messages: []
    }
  ]
}