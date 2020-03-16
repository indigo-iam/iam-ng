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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="REG_REQUESTS_ATTACHMENTS")
public class RegistrationRequestAttachmentEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  Long id;
  
  MetadataEntity metadata;
  
  @ManyToOne
  @JoinColumn(name = "REG_REQ_ID", nullable = false)
  RegistrationRequestEntity request;
  
  @Column(name = "LABEL", nullable = false, length = 36)
  String label;
  
  @Lob
  @Column(name="CONTENT", nullable = false)
  String content;
  
  public RegistrationRequestAttachmentEntity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MetadataEntity getMetadata() {
    return metadata;
  }

  public void setMetadata(MetadataEntity metadata) {
    this.metadata = metadata;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
  
  public RegistrationRequestEntity getRequest() {
    return request;
  }
  
  public void setRequest(RegistrationRequestEntity request) {
    this.request = request;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((label == null) ? 0 : label.hashCode());
    result = prime * result + ((request == null) ? 0 : request.hashCode());
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
    RegistrationRequestAttachmentEntity other = (RegistrationRequestAttachmentEntity) obj;
    if (label == null) {
      if (other.label != null)
        return false;
    } else if (!label.equals(other.label))
      return false;
    if (request == null) {
      if (other.request != null)
        return false;
    } else if (!request.equals(other.request))
      return false;
    return true;
  }

}
