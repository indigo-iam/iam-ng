# IAM NG development environment

This compose file sets up:

- a MySQL database, for the Keycloak and IAM API database
- a Keycloak instance, with three tenants configured
- the IAM API server

A safe startup procedure to have things running would be:

```bash
$ docker-compose up -d db 
$ docker-compose up -d kc
$ docker-compose up -d iam-api
$ docker-compose up -d nginx
```

## Configuration

Images are set in the .env file.

The Keycloak configuration needs to be run only once (the state is then
persisted in the database). Interesting environment variables:

| Env variable           | Meaning                                                                                    |
| ---------------------- | ------------------------------------------------------------------------------------------ |
| IAM_KC_SKIP_SETUP      | Skips Keycloak tenants configuration.                                                      |
| IAM_DASHBOARD_BASE_URL | Sets the dashboard base URL used to configure the iam-dashboard public client in KC realms |

## Service endpoints

Services now are proxied by an NGINX instance that does TLS termination and
virtual hosting.

| Service  | Endpoint                 |
| -------- | ------------------------ |
| Keycloak | https://kc.test.example  |
| IAM API  | https://api.test.example |

Add entries to your `/etc/hosts` file for the endpoints above to make
things work seamlessly.

The test X.509 certificate authority certificate used to sign the
wildcard certificate can be found
[here](./assets/trust/igi-test-ca.pem). Import this certificate in your browser and in curl trust roots.

## IAM API docs

API docs for the IAM API server can be found at
[here](https://api.test.example/swagger-ui/api-docs.html).

## Tenants and user accounts

The following tenants are created:

- iam
- alice
- atlas
- cms
- lhcb

In each tenant, an IAM administrator user is created with the following credentials:

- username: `admin`
- password: `password`

## Docker images

Docker images used in this compose are either available on Dockerhub and/or the
definition can be found in the ../docker folder.
