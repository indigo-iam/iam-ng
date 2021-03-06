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

import java.time.Clock;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "REALMS")
public class RealmEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME", unique = true, length = 255)
  private String name;

  @Column(name = "DESCRIPTION", length = 512)
  private String description;

  @Lob
  @Column(name = "CONFIG", nullable = false)
  private String configuration;

  private MetadataEntity metadata;
  
  @Column(name = "REGISTRATION_ENABLED", nullable = false)
  private boolean registrationEnabled;
  
  @Column(name = "PRIVACY_POLICY_URL", length = 128)
  private String privacyPolicyUrl;
  
  @Column(name = "AUP_URL", length = 128)
  private String aupUrl;

  @Column(name = "LOGO_URL", length = 128)
  private String logoUrl;
  
  public RealmEntity() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getConfiguration() {
    return configuration;
  }

  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }

  public MetadataEntity getMetadata() {
    return metadata;
  }

  public void setMetadata(MetadataEntity metadata) {
    this.metadata = metadata;
  }
  
  public boolean isRegistrationEnabled() {
    return registrationEnabled;
  }

  public void setRegistrationEnabled(boolean registrationEnabled) {
    this.registrationEnabled = registrationEnabled;
  }

  public String getPrivacyPolicyUrl() {
    return privacyPolicyUrl;
  }

  public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
    this.privacyPolicyUrl = privacyPolicyUrl;
  }

  public String getAupUrl() {
    return aupUrl;
  }

  public void setAupUrl(String aupUrl) {
    this.aupUrl = aupUrl;
  }
  
  public String getLogoUrl() {
    return logoUrl;
  }
  
  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }
  
  public void touch(Clock clock) {
    getMetadata().setLastUpdateTime(Date.from(clock.instant()));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    RealmEntity other = (RealmEntity) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

}
