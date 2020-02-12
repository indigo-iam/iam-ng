package it.infn.cnaf.sd.iam.persistence.realm.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

@Entity(name = "USERS")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "UUID", nullable = false, length = 36, unique = true)
  private String uuid;

  @Column(name = "USERNAME", nullable = false, length = 128, unique = true)
  private String username;

  @Column(name = "PASSWORD", length = 128)
  private String password;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  private MetadataEntity metadata;

  @Column(name = "PROVISIONED", nullable = false)
  private boolean provisioned = false;

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

  @ElementCollection
  @CollectionTable(indexes = {@Index(columnList = "EMAIL"), @Index(columnList = "LABEL")},
      name = "USERS_EMAILS", joinColumns = @JoinColumn(name = "USER_ID"))
  private Set<EmailEntity> emails = new HashSet<>();

  public UserEntity() {
    // TODO Auto-generated constructor stub
  }

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
    UserEntity other = (UserEntity) obj;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals(other.uuid))
      return false;
    return true;
  }

}
