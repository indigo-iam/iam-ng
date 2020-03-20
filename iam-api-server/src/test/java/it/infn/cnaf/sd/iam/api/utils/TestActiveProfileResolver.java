/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
