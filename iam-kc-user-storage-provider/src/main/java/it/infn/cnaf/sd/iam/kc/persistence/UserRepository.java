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
package it.infn.cnaf.sd.iam.kc.persistence;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;



@NamedQueries({@NamedQuery(name = UserRepository.QUERY_GET_USER_BY_USERNAME,
    query = "select u from UserEntity u where u.username = :username and u.realm.name = :realm"),})
public interface UserRepository {
  public static final String QUERY_GET_USER_BY_USERNAME = "getUserByUsername";
}
