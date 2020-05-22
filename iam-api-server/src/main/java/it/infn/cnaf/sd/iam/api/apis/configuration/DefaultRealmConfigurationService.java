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
package it.infn.cnaf.sd.iam.api.apis.configuration;

import java.time.Clock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.infn.cnaf.sd.iam.api.apis.configuration.dto.RealmConfigurationDTO;
import it.infn.cnaf.sd.iam.api.common.error.NotFoundError;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;
import it.infn.cnaf.sd.iam.persistence.repository.RealmRepository;

@Service
@Transactional
public class DefaultRealmConfigurationService implements RealmConfigurationService {

  private final Clock clock;
  private final ObjectMapper mapper;
  private final RealmRepository realmsRepo;

  @Autowired
  public DefaultRealmConfigurationService(Clock clock, ObjectMapper mapper, RealmRepository repo) {
    this.clock = clock;
    this.mapper = mapper;
    this.realmsRepo = repo;
  }

  private RealmConfigurationDTO parseJsonConfig(String jsonConfig) {
    try {
      return mapper.readValue(jsonConfig, RealmConfigurationDTO.class);
    } catch (JsonProcessingException e) {
      throw new RealmConfigurationError(
          "Error reading realm configuration from database: " + e.getMessage(), e);
    }
  }

  @Override
  public RealmConfigurationDTO getRealmConfiguration() {
    RealmEntity realm = RealmContext.getCurrentRealmEntity();
    return parseJsonConfig(realm.getConfiguration());
  }

  @Override
  public void saveRealmConfiguration(RealmConfigurationDTO realmConfig) {

    RealmEntity realm = RealmContext.getCurrentRealmEntity();

    try {
      realm.setConfiguration(mapper.writeValueAsString(realmConfig));
      realm.touch(clock);
    } catch (JsonProcessingException e) {
      throw new RealmConfigurationError(
          "Error persting realm configuration to database: " + e.getMessage(), e);
    }
  }

  @Override
  public RealmConfigurationDTO getRealmConfiguration(String realmName) {
    RealmEntity realm = realmsRepo.findByName(realmName)
      .orElseThrow(() -> new NotFoundError("Realm not found: " + realmName));

    return parseJsonConfig(realm.getConfiguration());
  }

}
