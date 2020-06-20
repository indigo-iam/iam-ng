# IAM NG development environment

This compose file sets up:

- a MySQL database, for the Keycloak and IAM API database
- a Keycloak instance, with three tenants configured
- the IAM API server

A safe startup procedure to have things running would be:

```bash
$ docker-compose up -d trust
$ docker-compose up -d db 
$ docker-compose up -d mailhog

# Wait that db and trust setup is over

$ docker-compose up -d kc
$ docker-compose up -d nginx
```

## Proxying the API server and Dashboard

Currently the nginx reverse proxy is configured to proxy the IAM dashboard and
API server running on the docker host but outside of the docker network.

To change this, and rely on daemons running in the docker network or elsewhere,
change the configuration [here](./assets/nginx).

## Configuration

Images are set in the .env file.

## Service endpoints

Services now are proxied by an NGINX instance that does TLS termination and
virtual hosting.

| Service  | Endpoint                 |
| -------- | ------------------------ |
| Keycloak | https://kc.local.io  |
| IAM API  | https://api.local.io |
| IAM Dashboard | https://iam.local.io |

Add entries to your `/etc/hosts` file for the endpoints above to make
things work seamlessly.

The test X.509 certificate authority certificate used to sign the
wildcard certificate can be found
[here](./assets/trust/igi-test-ca.pem). 

Import this certificate in your browser and in curl trust roots.

If running the IAM API server outside of docker, also make suer that the CA
certificate is part of the trust roots trusted by the JVM used to run the API
server.

## IAM API docs

API docs for the IAM API server can be found at
[here](https://api.local.io/swagger-ui/api-docs.html).

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
