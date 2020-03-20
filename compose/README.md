# IAM NG development environment

This compose file sets up:

- a MySQL database, for the Keycloak and IAM API database
- a Keycloak instance, with three tenants configured
- the IAM API server

Images are set in the .env file.

The Keycloak configuration needs to be run only once (the state is then persisted in the database).
Interesting envirnoment variables:

| Env variable           | Meaning                                                                                    |
| ---------------------- | ------------------------------------------------------------------------------------------ |
| IAM_KC_SKIP_SETUP      | Skips Keycloak tenants configuration.                                                      |
| IAM_DASHBOARD_BASE_URL | Sets the dashboard base URL used to configure the iam-dashboard public client in KC realms |

## Service endpoints

| Service        | port |
| -------------- | ---- |
| MySQL DB       | 3306 |
| Keycloak       | 8080 |
| IAM API server | 9876 |

The ports are bound to the Docker host (in many cases, bind to localhost).

API docs for the IAM API server can be found at [here](http://localhost:9876/swagger-ui/api-docs.html).

## Tenants and user accounts

The following tenants are created:

- iam
- alice
- cms

In each tenant, an IAM administrator user is created with the following credentials:

- username: `admin`
- password: `password`
