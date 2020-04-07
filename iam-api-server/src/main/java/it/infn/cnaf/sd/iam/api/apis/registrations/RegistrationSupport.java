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
package it.infn.cnaf.sd.iam.api.apis.registrations;

import java.util.function.Supplier;

import it.infn.cnaf.sd.iam.api.common.error.NotFoundError;

public interface RegistrationSupport {

  public static final String INVALID_REQUEST_REPRESENTATION = "Invalid request representation";
  public static final String INVALID_TOKEN = "Invalid token";
  public static final String REQUEST_CREATED = "Request created";
  public static final String REQUEST_CONFIRMED = "Request confirmed succesfully";

  public static final String AUTHENTICATION_ATTACHMENT_LABEL = "iam.authentication";

  default Supplier<NotFoundError> requestNotFoundForId(String requestId) {
    return () -> new NotFoundError(String.format("Request not found for id: %s", requestId));
  }

  default Supplier<NotFoundError> requestNotFoundForEmailChallenge(String emailChallenge) {
    return () -> new NotFoundError(
        String.format("No request found linked to email challenge: %s", emailChallenge));
  }


}
