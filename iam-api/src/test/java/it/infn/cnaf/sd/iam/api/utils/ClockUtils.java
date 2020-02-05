package it.infn.cnaf.sd.iam.api.utils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public interface ClockUtils {
  public static final Instant TEST_NOW = Instant.parse("2020-01-01T00:00:00.00Z");
  public static final Clock TEST_CLOCK = Clock.fixed(TEST_NOW, ZoneId.systemDefault()); 
}
