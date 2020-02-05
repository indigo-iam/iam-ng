package it.infn.cnaf.sd.iam.api.realm;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class RealmConfigDTO {

  String name;
  
  Date creationTime;
  
  Date lastUpdateTime;
  
  
  public RealmConfigDTO() {
    // empty constructor
  }

  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  @JsonSerialize
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
