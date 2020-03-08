package it.infn.cnaf.sd.iam.api.common.realm;

import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;

public class RealmContext {

  private static ThreadLocal<String> currentRealmName = new ThreadLocal<>();
  
  private static ThreadLocal<RealmEntity> currentRealmEntity = new ThreadLocal<>();

  public static void setCurrentRealmName(String realm) {
    currentRealmName.set(realm);
  }

  public static String getCurrentRealmName() {
    return currentRealmName.get();
  }

  public static void clear() {
    currentRealmName.remove();
    currentRealmEntity.remove();
  }

  public static void setCurrentRealmEntity(RealmEntity e) {
    currentRealmName.set(e.getName());
    currentRealmEntity.set(e);
  }
  
  public static RealmEntity getCurrentRealmEntity() {
    return currentRealmEntity.get();
  }
    
  private RealmContext() {
    // prevent instantiation
  }
}
