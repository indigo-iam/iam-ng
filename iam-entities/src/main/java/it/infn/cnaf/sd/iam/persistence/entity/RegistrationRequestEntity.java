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
package it.infn.cnaf.sd.iam.persistence.entity;

import static it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity.RegistrationRequestStatus.done;
import static it.infn.cnaf.sd.iam.persistence.entity.RequestOutcome.approved;
import static it.infn.cnaf.sd.iam.persistence.entity.RequestOutcome.rejected;

import java.io.Serializable;
import java.time.Clock;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "REG_REQUESTS",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"REALM_ID", "CHALLENGE"}),
        @UniqueConstraint(columnNames = {"REALM_ID", "EMAIL_CHALLENGE"})})
public class RegistrationRequestEntity implements Serializable {

  public enum RegistrationRequestStatus {
    created,
    confirmed,
    done
  }

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 36, unique = true)
  private String uuid;

  private MetadataEntity metadata;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REALM_ID", nullable = false)
  private RealmEntity realm;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false, length = 50)
  private RegistrationRequestStatus status;

  private RequesterInfoEntity requesterInfo;

  @ElementCollection
  @CollectionTable(name = "REG_REQUESTS_OTHERINFO")
  @MapKeyColumn(name = "OI_KEY")
  @Column(name = "OI_VAL")
  private Map<String, String> otherInfo = new HashMap<>();

  @ElementCollection
  @CollectionTable(
      indexes = {@Index(columnList = "L_PREFIX,L_NAME,L_VAL"),
          @Index(columnList = "L_PREFIX,L_NAME")},
      name = "REG_REQUESTS_LABELS", joinColumns = @JoinColumn(name = "REQ_ID"))
  @OrderBy("name")
  private Set<LabelEntity> labels = new HashSet<>();

  @ElementCollection
  @CollectionTable(name = "REG_REQUESTS_MESSAGES", joinColumns = @JoinColumn(name = "REQ_ID"))
  private Set<RequestMessageEntity> messages = new HashSet<>();

  @Column(name = "CHALLENGE", length = 36, unique = true, nullable = false)
  private String requestChallenge;

  @Column(name = "EMAIL_CHALLENGE", length = 36, unique = false, nullable = false)
  private String emailChallenge;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "request")
  private Set<RegistrationRequestAttachmentEntity> attachments = new HashSet<>();

  @Enumerated(EnumType.STRING)
  @Column(name = "OUTCOME", nullable = true, length = 16)
  private RequestOutcome outcome;
  
  public RegistrationRequestEntity() {}

  public Long getId() {
    return id;
  }


  public void setId(Long id) {
    this.id = id;
  }


  public String getUuid() {
    return uuid;
  }


  public void setUuid(String uuid) {
    this.uuid = uuid;
  }


  public MetadataEntity getMetadata() {
    return metadata;
  }


  public void setMetadata(MetadataEntity metadata) {
    this.metadata = metadata;
  }


  public RealmEntity getRealm() {
    return realm;
  }


  public void setRealm(RealmEntity realm) {
    this.realm = realm;
  }


  public RequesterInfoEntity getRequesterInfo() {
    return requesterInfo;
  }


  public void setRequesterInfo(RequesterInfoEntity requesterInfo) {
    this.requesterInfo = requesterInfo;
  }


  public Set<LabelEntity> getLabels() {
    return labels;
  }


  public void setLabels(Set<LabelEntity> labels) {
    this.labels = labels;
  }


  public Set<RequestMessageEntity> getMessages() {
    return messages;
  }


  public void setMessages(Set<RequestMessageEntity> messages) {
    this.messages = messages;
  }


  public String getRequestChallenge() {
    return requestChallenge;
  }


  public void setRequestChallenge(String requestChallenge) {
    this.requestChallenge = requestChallenge;
  }


  public String getEmailChallenge() {
    return emailChallenge;
  }


  public void setEmailChallenge(String emailChallenge) {
    this.emailChallenge = emailChallenge;
  }

  public void setOtherInfo(Map<String, String> otherInfo) {
    this.otherInfo = otherInfo;
  }

  public Map<String, String> getOtherInfo() {
    return otherInfo;
  }

  public Set<RegistrationRequestAttachmentEntity> getAttachments() {
    return attachments;
  }

  public void setAttachments(Set<RegistrationRequestAttachmentEntity> attachments) {
    this.attachments = attachments;
  }

  public RequestOutcome getOutcome() {
    return outcome;
  }
  
  public void setOutcome(RequestOutcome outcome) {
    this.outcome = outcome;
  }
  
  public void approve(Clock clock) {
    getMetadata().setLastUpdateTime(Date.from(clock.instant()));
    setStatus(done);
    setOutcome(approved);
  }
  
  public void reject(Clock clock) {
    getMetadata().setLastUpdateTime(Date.from(clock.instant()));
    setStatus(done);
    setOutcome(rejected);
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((realm == null) ? 0 : realm.hashCode());
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RegistrationRequestEntity other = (RegistrationRequestEntity) obj;
    if (realm == null) {
      if (other.realm != null)
        return false;
    } else if (!realm.equals(other.realm))
      return false;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals(other.uuid))
      return false;
    return true;
  }

  public void setLabel(String name, String value) {
    Optional<LabelEntity> statusLabel =
        getLabels().stream().filter(l -> name.equals(l.getName())).findAny();

    if (statusLabel.isPresent()) {
      statusLabel.get().setValue(value);
    } else {
      LabelEntity newLabel = LabelEntity.builder().name(name).value(value).build();
      getLabels().add(newLabel);
    }
  }

  public Optional<String> getLabel(String name) {
    return getLabels().stream()
      .filter(l -> name.equals(l.getName()))
      .findAny()
      .map(LabelEntity::getValue);
  }
  
  public RegistrationRequestStatus getStatus() {
    return status;
  }
  
  public void setStatus(RegistrationRequestStatus status) {
    this.status = status;
  }

}
