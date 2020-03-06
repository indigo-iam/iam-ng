package it.infn.cnaf.sd.iam.persistence.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "USERS",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"REALM_ID", "USERNAME"})})
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "UUID", nullable = false, length = 36, unique = true)
  private String uuid;

  @Column(name = "USERNAME", nullable = false, length = 128)
  private String username;

  @Column(name = "PASSWORD", length = 128)
  private String password;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  private MetadataEntity metadata;

  @Column(name = "PROVISIONED", nullable = false)
  private boolean provisioned = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REALM_ID")
  RealmEntity realm;

  @ManyToMany
  @JoinTable(name = "USERS_GROUPS",
      joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
      inverseJoinColumns = @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID"))
  @OrderBy("name")
  private Set<GroupEntity> groups = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "USERS_ROLES",
      joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
      inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
  @OrderBy("name")
  private Set<RoleEntity> roles = new HashSet<>();

  @ElementCollection
  @CollectionTable(indexes = {@Index(columnList = "A_NAME"), @Index(columnList = "A_NAME,A_VAL")},
      name = "USERS_ATTRIBUTES", joinColumns = @JoinColumn(name = "USER_ID"))
  private Set<AttributeEntity> attributes = new HashSet<>();

  @ElementCollection
  @CollectionTable(
      indexes = {@Index(columnList = "L_PREFIX,L_NAME,L_VAL"),
          @Index(columnList = "L_PREFIX,L_NAME")},
      name = "USERS_LABELS", joinColumns = @JoinColumn(name = "USER_ID"))
  private Set<LabelEntity> labels = new HashSet<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(indexes = {@Index(columnList = "EMAIL"), @Index(columnList = "LABEL")},
      name = "USERS_EMAILS", joinColumns = @JoinColumn(name = "USER_ID"))
  private Set<EmailEntity> emails = new HashSet<>();

  @ElementCollection
  @CollectionTable(indexes = {@Index(columnList = "PHONE_NUMBER"), @Index(columnList = "LABEL")},
      name = "USERS_PHONE_NUMBERS", joinColumns = @JoinColumn(name = "USER_ID"))
  private Set<PhoneNumberEntity> phoneNumbers = new HashSet<>();

  @Column(name = "GIVEN_NAME", length = 128, nullable = false)
  private String givenName;

  @Column(name = "FAMILY_NAME", length = 128, nullable = false)
  private String familyName;

  @Column(name = "MIDDLE_NAME", length = 128)
  private String middleName;

  @Column(name = "PROFILE", length = 128)
  private String profile;

  @Column(name = "PICTURE", length = 128)
  private String picture;

  @Column(name = "WEBSITE", length = 128)
  private String website;

  @Column(name = "GENDER", length = 1)
  private String gender;

  @Column(name = "ZONE_INFO")
  private String zoneinfo;

  @Column(name = "LOCALE")
  private String locale;
  
  @Column(name = "END_TIME", nullable = true)
  private Instant endTime;

  public UserEntity() {}

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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public MetadataEntity getMetadata() {
    return metadata;
  }

  public void setMetadata(MetadataEntity metadata) {
    this.metadata = metadata;
  }

  public boolean isProvisioned() {
    return provisioned;
  }

  public void setProvisioned(boolean provisioned) {
    this.provisioned = provisioned;
  }

  public Set<GroupEntity> getGroups() {
    return groups;
  }

  public void setGroups(Set<GroupEntity> groups) {
    this.groups = groups;
  }

  public Set<AttributeEntity> getAttributes() {
    return attributes;
  }

  public void setAttributes(Set<AttributeEntity> attributes) {
    this.attributes = attributes;
  }

  public Set<LabelEntity> getLabels() {
    return labels;
  }

  public void setLabels(Set<LabelEntity> labels) {
    this.labels = labels;
  }

  public void setEmails(Set<EmailEntity> emails) {
    this.emails = emails;
  }

  public Set<EmailEntity> getEmails() {
    return emails;
  }

  public Set<RoleEntity> getRoles() {
    return roles;
  }

  public void setRoles(Set<RoleEntity> roles) {
    this.roles = roles;
  }

  public Set<PhoneNumberEntity> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(Set<PhoneNumberEntity> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
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

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getZoneinfo() {
    return zoneinfo;
  }

  public void setZoneinfo(String zoneinfo) {
    this.zoneinfo = zoneinfo;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public RealmEntity getRealm() {
    return realm;
  }

  public void setRealm(RealmEntity realm) {
    this.realm = realm;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((realm == null) ? 0 : realm.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
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
    UserEntity other = (UserEntity) obj;
    if (realm == null) {
      if (other.realm != null)
        return false;
    } else if (!realm.equals(other.realm))
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }

}
