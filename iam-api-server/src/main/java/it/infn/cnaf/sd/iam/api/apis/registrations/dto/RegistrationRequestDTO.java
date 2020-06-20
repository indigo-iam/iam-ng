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
package it.infn.cnaf.sd.iam.api.apis.registrations.dto;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Null;

import com.google.common.collect.Lists;

import it.infn.cnaf.sd.iam.api.common.dto.BaseDTO;
import it.infn.cnaf.sd.iam.api.common.dto.LabelDTO;
import it.infn.cnaf.sd.iam.api.common.dto.MetadataDTO;
import it.infn.cnaf.sd.iam.api.common.dto.RealmDTO;

public class RegistrationRequestDTO extends BaseDTO {

  public static final String KIND = "RegistrationRequest";

  @Null
  private String id;
  
  @Null
  private String uuid;
  
  @Null
  private MetadataDTO metadata;
  
  @Null
  private RealmDTO realm;
  
  @Valid
  private RequesterInfoDTO requesterInfo;
  
  private Map<String, String> otherInfo = new HashMap<>();
  
  @Valid
  private Set<LabelDTO> labels = new LinkedHashSet<>();
  
  @Valid
  private List<RequestMessageDTO> messages = Lists.newArrayList();
  
  public RegistrationRequestDTO() {
    super(KIND);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public MetadataDTO getMetadata() {
    return metadata;
  }

  public void setMetadata(MetadataDTO metadata) {
    this.metadata = metadata;
  }

  public RealmDTO getRealm() {
    return realm;
  }

  public void setRealm(RealmDTO realm) {
    this.realm = realm;
  }

  public RequesterInfoDTO getRequesterInfo() {
    return requesterInfo;
  }

  public void setRequesterInfo(RequesterInfoDTO requesterInfo) {
    this.requesterInfo = requesterInfo;
  }

  public Map<String, String> getOtherInfo() {
    return otherInfo;
  }

  public void setOtherInfo(Map<String, String> otherInfo) {
    this.otherInfo = otherInfo;
  }

  public Set<LabelDTO> getLabels() {
    return labels;
  }

  public void setLabels(Set<LabelDTO> labels) {
    this.labels = labels;
  }
  
  public List<RequestMessageDTO> getMessages() {
    return messages;
  }
  
  public void setMessages(List<RequestMessageDTO> messages) {
    this.messages = messages;
  }

}
