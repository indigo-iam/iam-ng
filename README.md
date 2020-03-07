# IAM NG

## Running integration tests

By, default test run on H2 embedded database.

To run MySQL integration tests, use the following command:

```
mvn -Dspring.profiles.active=mysql-test test
```

MySQL integration tests require a recent docker installation.
