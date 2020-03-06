package it.infn.cnaf.sd.iam.kc;

import javax.naming.InitialContext;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

public class IamUserStorageProviderFactory
    implements UserStorageProviderFactory<IamUserStorageProvider> {

  public static final String IAM_USER_STORAGE_PROVIDER_ID = "iam-user-storage-provider";

  @Override
  public IamUserStorageProvider create(KeycloakSession session, ComponentModel model) {
    try {

      InitialContext ctx = new InitialContext();
      IamUserStorageProvider provider =
          (IamUserStorageProvider) ctx.lookup(String.format("java:global/%s/%s",
              IAM_USER_STORAGE_PROVIDER_ID, IamUserStorageProvider.class.getName()));

      provider.setModel(model);
      provider.setSession(session);

      return provider;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getId() {
    return IAM_USER_STORAGE_PROVIDER_ID;
  }

  @Override
  public String getHelpText() {
    return "IAM storage provider";
  }

}
