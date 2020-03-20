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

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import it.infn.cnaf.sd.iam.api.common.realm.DefaultRealmNameResolver;
import it.infn.cnaf.sd.iam.api.common.realm.RealmNameResolver;

@RunWith(MockitoJUnitRunner.class)
public class RealmResolverTests {

  @Mock
  HttpServletRequest request;
  
  RealmNameResolver realmResolver = new DefaultRealmNameResolver();
  
  @Test
  public void testEmptyRequestPath() throws Exception {
    
    when(request.getRequestURI()).thenReturn("");
    assertThat(realmResolver.resolveRealmName(request), nullValue());
    
    when(request.getRequestURI()).thenReturn("/");
    assertThat(realmResolver.resolveRealmName(request), nullValue());
    
    when(request.getRequestURI()).thenReturn("/over/the/top");
    assertThat(realmResolver.resolveRealmName(request), nullValue());
    
    when(request.getRequestURI()).thenReturn("/one");
    assertThat(realmResolver.resolveRealmName(request), nullValue());
    
    when(request.getRequestURI()).thenReturn("/api");
    assertThat(realmResolver.resolveRealmName(request), nullValue());
    
    when(request.getRequestURI()).thenReturn("/Realms/");
    assertThat(realmResolver.resolveRealmName(request), nullValue());
    
    when(request.getRequestURI()).thenReturn("/api ");
    assertThat(realmResolver.resolveRealmName(request), nullValue());
    
    when(request.getRequestURI()).thenReturn("/Realms/one");
    assertThat(realmResolver.resolveRealmName(request), is("one"));
    
    when(request.getRequestURI()).thenReturn("/Realms/two");
    assertThat(realmResolver.resolveRealmName(request), is("two"));
    
    when(request.getRequestURI()).thenReturn("/Realms/three");
    assertThat(realmResolver.resolveRealmName(request), is("three"));
    
    when(request.getRequestURI()).thenReturn("/apione");
    assertThat(realmResolver.resolveRealmName(request), nullValue());
    
    when(request.getRequestURI()).thenReturn("/Realms/one/groups");
    assertThat(realmResolver.resolveRealmName(request), is("one"));
    
    when(request.getRequestURI()).thenReturn("/Realms/three/groups");
    assertThat(realmResolver.resolveRealmName(request), is("three"));
  }

}
