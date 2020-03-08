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
package it.infn.cnaf.sd.iam.kc;

import javax.naming.InitialContext;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

public class IamUserStorageProviderFactory
    implements UserStorageProviderFactory<IamUserStorageProvider> {

  public static final String IAM_USER_STORAGE_PROVIDER_ID = "iam-user-storage-provider";

  @Override
  public IamUserStorageProvider create(KeycloakSession session, ComponentModel model) {
    try {

      InitialContext ctx = new InitialContext();
      IamUserStorageProvider provider =
          (IamUserStorageProvider) ctx.lookup(String.format("java:global/%s/%s",
              IAM_USER_STORAGE_PROVIDER_ID, IamUserStorageProvider.class.getName()));

      provider.setModel(model);
      provider.setSession(session);

      return provider;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getId() {
    return IAM_USER_STORAGE_PROVIDER_ID;
  }

  @Override
  public String getHelpText() {
    return "IAM storage provider";
  }

}
