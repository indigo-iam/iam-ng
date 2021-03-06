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
import javax.persistence.Embeddable;

@Embeddable
public class RequesterInfoEntity {

  @Column(name = "USERNAME", length = 128, nullable = false)
  private String username;

  @Column(name = "GIVEN_NAME", length = 128, nullable = false)
  private String givenName;

  @Column(name = "FAMILY_NAME", length = 128, nullable = false)
  private String familyName;

  @Column(name = "EMAIL", nullable = false, length = 128)
  private String email;

  @Column(name = "EMAIL_VERIFIED", nullable = false)
  private boolean emailVerified;

  public RequesterInfoEntity() {}

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public String getName() {
    return String.format("%s %s", getGivenName(), getFamilyName());
  }

}
