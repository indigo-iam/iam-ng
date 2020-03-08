# IAM NG

## Running integration tests

By, default test run on H2 embedded database.

To run MySQL integration tests, use the following command:

```
mvn -Dspring.profiles.active=mysql-test test
```

MySQL integration tests require a recent docker installation.

## Generating the schema code from Hibernate

Start the api with the ddl profile.
Hibernate is configured to print DDL statements to the standard output.
Copy all the lines starting with "Hibernate: " and, with the following 
script, you get a suitable Flyway migration script:

```bash
pbpaste | grep "^Hibernate:" | sed -e "s/^Hibernate: //" -e "s/engine=InnoDB//" | sed "s/$/;/" | grep -v drop | pbcopy
```
