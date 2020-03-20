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
package it.infn.cnaf.sd.iam.api.apis.users;

import static java.lang.String.format;

public interface UserSupport {

  public static final String INVALID_USER_REPRESENTATION = "Invalid user";
  public static final String USER_NOT_FOUND_ERROR_TEMPLATE = "User not found: %s";

  public default String userNotFoundMessage(String uuidOrName) {
    return format(USER_NOT_FOUND_ERROR_TEMPLATE, uuidOrName);
  }
}
