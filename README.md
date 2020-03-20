# IAM NG

This repo hosts the initial code of the IAM NG API and Keycloak integration
modules.

This code is in early stages of development.


## Developer info

### Changing the version number

To change the version number, update the `revision` property in the
[iam-dependencies pom.xml file](./project/iam-dependencies/pom.xml).

The `mvn versions` plugin does not work with the current approach.

### Docker compose development environment

In the [compose](./compose) folder, a docker-compose can be found
to bootstrap a development environment, composed of:

- MySQL database, for the Keycloak and IAM API database
- a Keycloak instance, with three tenants configured
- IAM API

Check instructions on how to use it [here](./compose/README.md).

### Running IAM API integration tests

By, default test run on H2 embedded database.

To run MySQL integration tests, use the following command:

```
mvn -Dspring.profiles.active=mysql-test-tc test
```

This will boostrap a MySQL container using [Testcontainers][testcontainers],
a recent Docker installation is required.

### Generating the schema code from Hibernate

Start the api with the ddl profile.
Hibernate is configured to print DDL statements to the standard output.
Copy all the lines starting with "Hibernate: " and, with the following 
script, you get a suitable Flyway migration script:

```bash
pbpaste | grep "^Hibernate:" | sed -e "s/^Hibernate: //" -e "s/engine=InnoDB//" | sed "s/$/;/" | grep -v drop | pbcopy
```
