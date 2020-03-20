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
package it.infn.cnaf.sd.iam.api.users;

import org.assertj.core.util.Lists;

import it.infn.cnaf.sd.iam.api.apis.users.dto.EmailDTO;
import it.infn.cnaf.sd.iam.api.apis.users.dto.UserDTO;

public interface UserTestSupport {
  
  public static final String ALICE_REALM = "alice";

  public static final String LENNON_GIVEN_NAME = "John";
  public static final String LENNON_FAMILY_NAME = "Lennon";
  public static final String LENNON_USERNAME = "lennon";
  public static final String LENNON_EMAIL = "lennon@apple-records.com";

  public static final String EXPECTED_USER_NOT_FOUND = "Expected user not found";

  default UserDTO lennonUser() {
    UserDTO u = new UserDTO();
    u.setGivenName(LENNON_GIVEN_NAME);
    u.setFamilyName(LENNON_FAMILY_NAME);
    u.setUsername(LENNON_USERNAME);
    EmailDTO email = new EmailDTO();
    email.setEmail(LENNON_EMAIL);
    u.setEmails(Lists.newArrayList(email));
    return u;
  }
}
