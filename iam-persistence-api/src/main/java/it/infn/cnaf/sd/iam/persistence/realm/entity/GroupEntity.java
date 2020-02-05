package it.infn.cnaf.sd.iam.persistence.realm.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "GROUPS")
public class GroupEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "UUID", nullable = false, length = 36, unique = true)
  private String uuid;

  @Column(name = "NAME", nullable = false, length = 512, unique = true)
  private String name;

  @Column(name = "DESCRIPTION", nullable = true, length = 512)
  private String description;

  private MetadataEntity metadata;

  @ManyToOne
  @JoinColumn(name = "PARENT_GROUP_ID", nullable = true)
  private GroupEntity parentGroup;

  @OneToMany(mappedBy = "parentGroup", cascade = CascadeType.PERSIST)
  private Set<GroupEntity> childrenGroups = new HashSet<>();

  @ElementCollection
  @CollectionTable(indexes = {@Index(columnList = "A_NAME"), @Index(columnList = "A_NAME,A_VAL")},
      name = "GROUPS_ATTRIBUTES", joinColumns = @JoinColumn(name = "GROUP_ID"))
  private Set<AttributeEntity> attributes = new HashSet<>();

  @ElementCollection
  @CollectionTable(
      indexes = {@Index(columnList = "L_PREFIX,L_NAME,L_VAL"),
          @Index(columnList = "L_PREFIX,L_NAME")},
      name = "GROUPS_LABELS", joinColumns = @JoinColumn(name = "GROUP_ID"))
  private Set<LabelEntity> labels = new HashSet<>();

  public GroupEntity() {}

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

  public MetadataEntity getMetadata() {
    return metadata;
  }

  public void setMetadata(MetadataEntity metadata) {
    this.metadata = metadata;
  }

  public Set<LabelEntity> getLabels() {
    return labels;
  }

  public void setLabels(Set<LabelEntity> labels) {
    this.labels = labels;
  }

  public GroupEntity getParentGroup() {
    return parentGroup;
  }

  public void setParentGroup(GroupEntity parentGroup) {
    this.parentGroup = parentGroup;
  }

  public Set<GroupEntity> getChildrenGroups() {
    return childrenGroups;
  }

  public void setChildrenGroups(Set<GroupEntity> childrenGroups) {
    this.childrenGroups = childrenGroups;
  }

  public void setAttributes(Set<AttributeEntity> attributes) {
    this.attributes = attributes;
  }

  public Set<AttributeEntity> getAttributes() {
    return attributes;
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
    GroupEntity other = (GroupEntity) obj;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals(other.uuid))
      return false;
    return true;
  }

}
