package it.infn.cnaf.sd.iam.api.oauth;

import java.net.URL;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import it.infn.cnaf.sd.iam.api.common.utils.RealmUtils;

public class RealmAwareTokenIssuerValidator implements OAuth2TokenValidator<Jwt> {

  private static OAuth2Error INVALID_ISSUER = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST,
      "Invalid token issuer", "https://tools.ietf.org/html/rfc6750#section-3.1");

  private final OAuth2TokenValidatorResult SUCCESS = OAuth2TokenValidatorResult.success();
  private final RealmUtils realmUtils;

  public RealmAwareTokenIssuerValidator(RealmUtils utils) {
    this.realmUtils = utils;
  }

  @Override
  public OAuth2TokenValidatorResult validate(Jwt token) {
    final URL expectedIssuer = realmUtils.realmTrustedTokenIssuer();
    if (expectedIssuer.equals(token.getIssuer())) {
      return SUCCESS;
    } else {
      return OAuth2TokenValidatorResult.failure(INVALID_ISSUER);
    }
  }
}
