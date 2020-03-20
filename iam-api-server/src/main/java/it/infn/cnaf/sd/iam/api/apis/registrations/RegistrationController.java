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

import static it.infn.cnaf.sd.iam.api.common.utils.ValidationHelper.handleValidationError;
import static org.springframework.http.HttpStatus.CREATED;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.infn.cnaf.sd.iam.api.apis.error.ErrorUtils;
import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationConfigurationDTO;
import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationRequestCreationResultDTO;
import it.infn.cnaf.sd.iam.api.apis.registrations.dto.RegistrationRequestDTO;
import it.infn.cnaf.sd.iam.api.common.dto.MessageResultDTO;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;

@RestController
@RequestMapping(value = "/Realms/{realm}")
@Transactional
public class RegistrationController implements RegistrationSupport, ErrorUtils {

  private final RegistrationService service;
  private final RegistrationRequestMapper mapper;

  @Autowired
  public RegistrationController(RegistrationService service, RegistrationRequestMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @PostMapping("/Registrations")
  @ResponseStatus(CREATED)
  public RegistrationRequestCreationResultDTO submitRequest(
      @RequestBody @Validated final RegistrationRequestDTO dto,
      final BindingResult validationResult, final Authentication authentication) {

    handleValidationError(INVALID_REQUEST_REPRESENTATION, validationResult);
    RegistrationRequestEntity request =
        service.createRegistrationRequest(mapper.dtoToEntity(dto), authentication);

    return RegistrationRequestCreationResultDTO.builder()
      .message(REQUEST_CREATED)
      .requestId(request.getUuid())
      .requestChallenge(request.getRequestChallenge())
      .build();
  }

  @GetMapping("/Registrations/{requestId}/confirm")
  public MessageResultDTO confirmEmailAddress(@PathVariable String requestId,
      @RequestParam @Validated @Size(min = 36, max = 36) String token,
      final BindingResult validationResult) {

    handleValidationError(INVALID_TOKEN, validationResult);

    return null;
  }

  @GetMapping("/Registrations/config")
  public RegistrationConfigurationDTO getConfiguration() {
    RealmEntity realm = RealmContext.getCurrentRealmEntity();

    return RegistrationConfigurationDTO.builder()
      .aupUrl(realm.getAupUrl())
      .privacyPolicyUrl(realm.getPrivacyPolicyUrl())
      .registrationEnabled(realm.isRegistrationEnabled())
      .logoUrl(realm.getLogoUrl())
      .build();
  }

}
