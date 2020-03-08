package it.infn.cnaf.sd.iam.api.common.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import org.springframework.stereotype.Component;

import it.infn.cnaf.sd.iam.api.common.error.InvalidRequestError;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.api.properties.IamProperties;

@Component
public class RealmUtils {

  IamProperties properties;

  public RealmUtils(IamProperties properties) {
    this.properties = properties;
  }

  public URL realmTrustedTokenIssuer() {
    String currentRealm = RealmContext.getCurrentRealmName();
    if (Objects.isNull(currentRealm)) {
      throw new InvalidRequestError("Unspecified realm");
    }

    try {
      return new URL(String.format("%s/%s", properties.getKeycloakBaseUrl(), currentRealm));
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
  }

}
