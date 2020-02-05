package it.infn.cnaf.sd.iam.api.utils;

import static java.util.Collections.singletonMap;

import java.time.Duration;

import org.testcontainers.containers.MySQLContainer;

public class IamMySqlContainer extends MySQLContainer<IamMySqlContainer> {

  public static final String DEFAULT_IMAGE = "indigoiam/testdb:latest";
  public static final String DEFAULT_DB_NAME = "iam";
  public static final String DEFAULT_USER = "iam";
  public static final String DEFAULT_PASSWORD = "pwd";
  public static final int DEFAULT_STARTUP_TIMEOUT = 30;

  public IamMySqlContainer() {
    this(DEFAULT_IMAGE);
  }

  @Override
  protected void configure() {
    addExposedPort(MYSQL_PORT);
    setStartupAttempts(1);
  }

  public IamMySqlContainer(String imageName) {
    super(imageName);
    withUsername(DEFAULT_USER).withPassword(DEFAULT_PASSWORD)
      .withDatabaseName(DEFAULT_DB_NAME)
      .withStartupTimeout(Duration.ofSeconds(DEFAULT_STARTUP_TIMEOUT))
      .withTmpFs(singletonMap("/var/lib/mysql", "rw"));
  }

  public String getJdbcUrl(String dbName) {
    int port = getFirstMappedPort();
    String ipAddress = getContainerIpAddress();
    return String.format("jdbc:mysql://%s:%d/%s", ipAddress, port, dbName);
  }

  @Override
  public void stop() {
    // Leave container running
  }

}
