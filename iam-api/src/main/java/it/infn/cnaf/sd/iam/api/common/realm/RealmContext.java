package it.infn.cnaf.sd.iam.api.common.realm;

public class RealmContext {

  private static ThreadLocal<String> currentRealm = new ThreadLocal<>();

  public static void setCurrentRealm(String realm) {
    currentRealm.set(realm);
  }

  public static String getCurrentRealm() {
    return currentRealm.get();
  }

  public static void clear() {
    currentRealm.remove();
  }

  private RealmContext() {
    // prevent instantiation
  }
}
