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
package it.infn.cnaf.sd.iam.api.apis.realms;

import org.mapstruct.Mapper;

import it.infn.cnaf.sd.iam.api.common.DtoEntityMapper;
import it.infn.cnaf.sd.iam.api.common.dto.RealmDTO;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;

@Mapper(componentModel = "spring")
public interface RealmMapper extends DtoEntityMapper<RealmDTO, RealmEntity> {

}
