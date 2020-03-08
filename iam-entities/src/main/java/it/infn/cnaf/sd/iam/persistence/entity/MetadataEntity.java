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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class MetadataEntity {

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATION_TIME", nullable = false)
  private Date creationTime;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "LAST_UPDATE_TIME", nullable = false)
  private Date lastUpdateTime;

  public MetadataEntity() {}

  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public Date getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Date lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public static MetadataEntity fromCurrentInstant(Clock clock) {
    final Date now = Date.from(clock.instant());
    MetadataEntity e = new MetadataEntity();
    e.setCreationTime(now);
    e.setLastUpdateTime(now);
    return e;
  }
}
