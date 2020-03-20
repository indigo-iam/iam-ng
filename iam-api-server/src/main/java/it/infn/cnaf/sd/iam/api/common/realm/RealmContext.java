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
package it.infn.cnaf.sd.iam.api.common.realm;

import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;

public class RealmContext {

  private static ThreadLocal<String> currentRealmName = new ThreadLocal<>();
  
  private static ThreadLocal<RealmEntity> currentRealmEntity = new ThreadLocal<>();

  public static void setCurrentRealmName(String realm) {
    currentRealmName.set(realm);
  }

  public static String getCurrentRealmName() {
    return currentRealmName.get();
  }

  public static void clear() {
    currentRealmName.remove();
    currentRealmEntity.remove();
  }

  public static void setCurrentRealmEntity(RealmEntity e) {
    currentRealmName.set(e.getName());
    currentRealmEntity.set(e);
  }
  
  public static RealmEntity getCurrentRealmEntity() {
    return currentRealmEntity.get();
  }
    
  private RealmContext() {
    // prevent instantiation
  }
}
