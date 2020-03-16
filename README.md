# IAM NG

This repo hosts the initial code of the IAM NG API and Keycloak integration
modules.

This code is early stages of development.


## Developer info

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
