package it.infn.cnaf.sd.iam.api.utils;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Set;

import org.springframework.test.context.support.DefaultActiveProfilesResolver;

import com.google.common.base.Strings;

public class TestActiveProfileResolver extends DefaultActiveProfilesResolver {

  public static final String SPRING_PROFILES_ACTIVE_PROPERTY = "spring.profiles.active";

  public static final String COMMA = ",";

  public static final String H2_PROFILE = "h2";

  public static final String[] DEFAULT_PROFILES = {H2_PROFILE};


  Set<String> getSpringProfiles() {

    String springProfiles = System.getProperty(SPRING_PROFILES_ACTIVE_PROPERTY);
    if (!Strings.isNullOrEmpty(springProfiles)) {
      return Arrays.stream(springProfiles.split(COMMA)).map(String::trim).collect(toSet());
    }

    return emptySet();
  }

  @Override
  public String[] resolve(Class<?> testClass) {
    String[] resolvedProfiles = super.resolve(testClass);
    Set<String> springProfiles = getSpringProfiles();

    if (resolvedProfiles.length == 0) {
      if (springProfiles.isEmpty()) {
        return DEFAULT_PROFILES;
      } else {
        return springProfiles.toArray(new String[] {});
      }
    } else {
      return resolvedProfiles;
    }
  }

}
