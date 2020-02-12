package it.infn.cnaf.sd.iam.persistence.realm.entity;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "CONFIG")
public class ConfigurationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME", nullable = false, length = 128, unique = true)
  private String name;

  private MetadataEntity metadata;

  @Column(name = "DESCRIPTION", length = 512)
  private String description;
  
  @Lob
  @Column(name = "CONFIG", nullable = false)
  private String configuration;

  public ConfigurationEntity() {
    // Empty constructor
  }

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

  public MetadataEntity getMetadata() {
    return metadata;
  }

  public void setMetadata(MetadataEntity metadata) {
    this.metadata = metadata;
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

  @Override
  @Generated("eclipse")
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  @Generated("eclipse")
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ConfigurationEntity other = (ConfigurationEntity) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "ConfigurationEntity [id=" + id + ", name=" + name + "]";
  }

  public static final ConfigurationEntity withName(String name) {
    ConfigurationEntity re = new ConfigurationEntity();
    re.setName(name);
    return re;
  }
}
