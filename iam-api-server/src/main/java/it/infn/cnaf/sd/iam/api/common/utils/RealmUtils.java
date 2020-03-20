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
package it.infn.cnaf.sd.iam.api.common.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import org.springframework.stereotype.Component;

import it.infn.cnaf.sd.iam.api.common.error.InvalidRequestError;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.api.properties.IamProperties;

@Component
public class RealmUtils {

  IamProperties properties;

  public RealmUtils(IamProperties properties) {
    this.properties = properties;
  }

  public URL realmTrustedTokenIssuer() {
    String currentRealm = RealmContext.getCurrentRealmName();
    if (Objects.isNull(currentRealm)) {
      throw new InvalidRequestError("Unspecified realm");
    }

    try {
      return new URL(String.format("%s/%s", properties.getKeycloakBaseUrl(), currentRealm));
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
  }

}
