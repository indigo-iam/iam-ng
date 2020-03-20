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
package it.infn.cnaf.sd.iam.api.registration;

import static it.infn.cnaf.sd.iam.api.common.utils.PageUtils.buildPageRequest;
import static it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus.confirmed;
import static it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus.created;
import static it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus.done;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import it.infn.cnaf.sd.iam.api.utils.ClockUtils;
import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.IntegrationTestSupport;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RequestMessageEntity;
import it.infn.cnaf.sd.iam.persistence.repository.RealmRepository;
import it.infn.cnaf.sd.iam.persistence.repository.RegistrationRequestRepository;

@RunWith(SpringRunner.class)
@IamTest
public class RegistrationRequestJPATests extends IntegrationTestSupport
    implements RegistrationRequestTestUtils, ClockUtils {

  @Autowired
  EntityManager em;

  @Autowired
  RealmRepository realmRepo;

  @Autowired
  RegistrationRequestRepository requestRepo;

  @Test
  public void dbStartsEmpty() {
    assertThat(requestRepo.count(), is(0L));

    RealmEntity realm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    final String uuid = UUID.randomUUID().toString();
    RegistrationRequestEntity req = newTemplateRequest(TEST_CLOCK, realm, uuid, 0);
    requestRepo.save(req);

    assertThat(requestRepo.count(), is(1L));

    Page<RegistrationRequestEntity> requests = requestRepo.findByRealmName(REALM_ALICE,
        buildPageRequest(10, 0, 10, SORT_BY_CREATION_TIME));

    assertThat(requests.getTotalElements(), is(1L));

    RegistrationRequestEntity savedReq = requests.getContent().get(0);

    assertThat(savedReq.getUuid(), is(uuid));
    assertThat(savedReq.getRequesterInfo(), notNullValue());
    assertThat(savedReq.getRequesterInfo().getGivenName(), is(U0_GIVEN_NAME));
    assertThat(savedReq.getRequesterInfo().getFamilyName(), is(U0_FAMILY_NAME));
    assertThat(savedReq.getRequesterInfo().getUsername(), is(U0_USERNAME));
    assertThat(savedReq.getRequesterInfo().getEmail(), is(U0_EMAIL));
    assertThat(savedReq.getMetadata().getCreationTime(), is(TEST_NOW_DATE));
    assertThat(savedReq.getMetadata().getLastUpdateTime(), is(TEST_NOW_DATE));
    assertThat(savedReq.getLabels(), empty());
    assertThat(savedReq.getAttachments(), empty());
    assertThat(savedReq.getMessages(), hasSize(1));

    RequestMessageEntity message = savedReq.getMessages().iterator().next();
    assertThat(message.getSender(), nullValue());
    assertThat(message.getCreationTime(), is(TEST_NOW_DATE));
    assertThat(message.getMessage(), is(U0_MESSAGE));

    savedReq.getMessages().clear();
    requestRepo.save(savedReq);

    savedReq = requestRepo.findByRealmNameAndUuid(REALM_ALICE, uuid)
      .orElseThrow(assertionError(ERROR_EXPECTED_REQUEST_NOT_FOUND));

    assertThat(savedReq.getMessages(), empty());
  }

  @Test
  public void realmResolutionWorkAsExpected() {
    assertThat(requestRepo.count(), is(0L));

    RealmEntity aliceRealm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RealmEntity cmsRealm =
        realmRepo.findByName(REALM_CMS).orElseThrow(realmNotFoundError(REALM_CMS));

    for (int i = 0; i < 10; i++) {
      final String uuid = UUID.randomUUID().toString();
      RegistrationRequestEntity req = newTemplateRequest(TEST_CLOCK, aliceRealm, uuid, i);
      requestRepo.save(req);
    }

    assertThat(requestRepo.count(), is(10L));

    Page<RegistrationRequestEntity> aliceRequests = requestRepo.findByRealmName(REALM_ALICE,
        buildPageRequest(10, 0, 10, SORT_BY_CREATION_TIME));

    assertThat(aliceRequests.getTotalElements(), is(10L));

    aliceRequests.forEach(r -> {
      assertThat(r.getRealm(), is(aliceRealm));
    });

    for (int i = 0; i < 10; i++) {
      final String uuid = UUID.randomUUID().toString();
      RegistrationRequestEntity req = newTemplateRequest(TEST_CLOCK, cmsRealm, uuid, i);
      requestRepo.save(req);
    }

    assertThat(requestRepo.count(), is(20L));

    Page<RegistrationRequestEntity> cmsRequests =
        requestRepo.findByRealmName(REALM_CMS, buildPageRequest(10, 0, 10, SORT_BY_CREATION_TIME));

    assertThat(cmsRequests.getTotalElements(), is(10L));

    cmsRequests.forEach(r -> {
      assertThat(r.getRealm(), is(cmsRealm));
      requestRepo.delete(r);
    });

    assertThat(requestRepo.count(), is(10L));

  }

  @Test
  public void labelResolutionWorkAsExpected() {
    assertThat(requestRepo.count(), is(0L));

    RealmEntity aliceRealm =
        realmRepo.findByName(REALM_ALICE).orElseThrow(realmNotFoundError(REALM_ALICE));

    RealmEntity cmsRealm =
        realmRepo.findByName(REALM_CMS).orElseThrow(realmNotFoundError(REALM_CMS));

    Stream.of(aliceRealm, cmsRealm).forEach(r -> {
      for (int i = 0; i < 10; i++) {
        String uuid = UUID.randomUUID().toString();
        RegistrationRequestEntity req = newTemplateRequest(TEST_CLOCK, r, uuid, 0);
        if (i == 0) {
          req.setLabel("test", "test");
        }
        requestRepo.save(req);
      }
    });

    assertThat(requestRepo.count(), is(20L));

    Stream.of(aliceRealm, cmsRealm).forEach(r -> {
      Page<RegistrationRequestEntity> requests = requestRepo.findByRealmName(r.getName(),
          buildPageRequest(10, 0, 10, SORT_BY_CREATION_TIME));

      assertThat(requests.getTotalElements(), is(10L));

      Page<RegistrationRequestEntity> testRequests =
          requestRepo.findByRealmNameAndLabelValue(r.getName(), "test", "test",
              buildPageRequest(10, 0, 10, SORT_BY_CREATION_TIME));

      assertThat(testRequests.getTotalElements(), is(1L));
    });

    Page<RegistrationRequestEntity> aliceRequests = requestRepo.findByRealmNameAndStatus(REALM_ALICE,
        created, buildPageRequest(10, 0, 10, SORT_BY_CREATION_TIME));

    aliceRequests.getContent().forEach(r -> {
      r.setStatus(confirmed);
      requestRepo.save(r);
    });

    aliceRequests = requestRepo.findByRealmNameAndStatus(REALM_ALICE,
        confirmed, buildPageRequest(10, 0, 10, SORT_BY_CREATION_TIME));

    assertThat(aliceRequests.getTotalElements(), is(10L));

    Page<RegistrationRequestEntity> cmsRequests = requestRepo.findByRealmNameAndStatus(REALM_CMS,
        created, buildPageRequest(10, 0, 10, SORT_BY_CREATION_TIME));

    assertThat(cmsRequests.getTotalElements(), is(10L));

    for (int i = 100; i < 200; i++) {
      String uuid = UUID.randomUUID().toString();
      RegistrationRequestEntity req = newTemplateRequest(TEST_CLOCK, aliceRealm, uuid, 100);
      req.setStatus(done);
      req.setLabel("flow", "test");
      requestRepo.save(req);
      em.flush();
    }

    assertThat(requestRepo.count(), is(120L));

    aliceRequests = requestRepo.findByRealmNameAndStatus(REALM_ALICE,
        created, buildPageRequest(10, 0, 10, SORT_BY_CREATION_TIME)); 

    assertThat(aliceRequests.getTotalElements(), is(0L));

    aliceRequests = requestRepo.findByRealmNameAndLabelValue(REALM_ALICE, "flow", "test",
        buildPageRequest(10, 0, 10, SORT_BY_CREATION_TIME));

    assertThat(aliceRequests.getTotalElements(), is(100L));

    aliceRequests = requestRepo.findByRealmNameAndStatus(REALM_ALICE,
        done, buildPageRequest(10, 0, 10, SORT_BY_CREATION_TIME));
    
    assertThat(aliceRequests.getTotalElements(), is(100L));

  }



}
