package it.infn.cnaf.sd.iam.api.common.dto;

import java.util.Date;

public class MetadataDTO {

  Date creationTime;

  Date lastUpdateTime;

  public MetadataDTO() {}

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
}
