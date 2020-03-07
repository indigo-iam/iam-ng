/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare, 2014-2020.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.infn.cnaf.sd.iam.api.oauth;

import static java.lang.String.format;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import com.google.common.cache.LoadingCache;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

import it.infn.cnaf.sd.iam.api.common.utils.RealmUtils;

public class CompositeJwtDecoder implements JwtDecoder {
  
  private static final String DECODING_ERROR_MESSAGE_TEMPLATE =
      "An error occurred while attempting to decode the Jwt: %s";

  public static final Logger LOG = LoggerFactory.getLogger(CompositeJwtDecoder.class);

  final LoadingCache<String, JwtDecoder> decoders;
  final RealmUtils realmUtils;
  
  public CompositeJwtDecoder(LoadingCache<String, JwtDecoder> decoders, RealmUtils realmUtils) {
    this.decoders = decoders;
    this.realmUtils = realmUtils;
  }
  
  protected JwtDecoder resolveDecoder(String token) {

    String issuer = resolveIssuerFromToken(token);
    
    if (!realmUtils.realmTrustedTokenIssuer().toString().equals(issuer)) {
      throw new UnknownTokenIssuerError(issuer); 
    }
    
    try {
      return decoders.get(realmUtils.realmTrustedTokenIssuer().toString());
    } catch (ExecutionException e) {
      LOG.warn("Error resolving OAuth issuer configuration for {}", issuer);
      if (LOG.isDebugEnabled()) {
        LOG.warn("Error resolving OAuth issuer configuration for {}", issuer,e);
      }
      throw new UnknownTokenIssuerError(issuer);
    }
  }
  
  @Override
  public Jwt decode(String token) {
    JwtDecoder decoder = resolveDecoder(token);
    return decoder.decode(token);
  }
  
  private String resolveIssuerFromToken(String token) {
      try {
        JWT jwt;
        jwt = JWTParser.parse(token);
        return jwt.getJWTClaimsSet().getIssuer();
      } catch (ParseException e) {
        if (LOG.isDebugEnabled()) {
          LOG.error(format(DECODING_ERROR_MESSAGE_TEMPLATE, e.getMessage()));
        }
        throw new JwtException(format(DECODING_ERROR_MESSAGE_TEMPLATE, e.getMessage()), e);
      } 
  }

}
