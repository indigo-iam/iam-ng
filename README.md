# IAM NG

## Running integration tests

By, default test run on H2 embedded database.

To run MySQL integration tests, set the `test-groups` system property as
follows:

```
mvn -Dtest-groups=mysql-integration test
```

MySQL integration tests require a recent docker installation.
