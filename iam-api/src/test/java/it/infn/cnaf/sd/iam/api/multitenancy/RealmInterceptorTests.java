/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2020
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
package it.infn.cnaf.sd.iam.api.multitenancy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import it.infn.cnaf.sd.iam.api.common.error.InvalidRequestError;
import it.infn.cnaf.sd.iam.api.common.error.NotFoundError;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.api.common.realm.RealmInterceptor;
import it.infn.cnaf.sd.iam.api.common.realm.RealmNameResolver;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;
import it.infn.cnaf.sd.iam.persistence.repository.RealmRepository;

@RunWith(MockitoJUnitRunner.class)
public class RealmInterceptorTests {

  @Mock
  RealmRepository repo;

  @Mock
  RealmNameResolver resolver;

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  Object handler;

  @InjectMocks
  RealmInterceptor interceptor;

  @Mock
  Exception exception;

  @Before
  public void setup() {
    RealmContext.clear();
  }


  @Test(expected = InvalidRequestError.class)
  public void testNullRealmThrowsException() throws Exception {
    when(resolver.resolveRealmName(request)).thenReturn(null);
    interceptor.preHandle(request, response, handler);
  }

  @Test(expected = NotFoundError.class)
  public void testUnknownRealmThrowsException() throws Exception {
    when(resolver.resolveRealmName(request)).thenReturn("test");
    when(repo.findByName("test")).thenReturn(Optional.empty());

    interceptor.preHandle(request, response, handler);
  }


  @Test
  public void testRealmFoundSetsRealmInRequest() throws Exception {
    RealmEntity testRealm = new RealmEntity();
    testRealm.setName("test");

    when(resolver.resolveRealmName(request)).thenReturn("test");
    when(repo.findByName("test")).thenReturn(Optional.of(testRealm));

    interceptor.preHandle(request, response, handler);
    
    assertThat(RealmContext.getCurrentRealmName(), is("test"));

    interceptor.afterCompletion(request, response, testRealm, exception);

    assertThat(RealmContext.getCurrentRealmName(), nullValue());

  }

}
