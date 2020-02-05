package it.infn.cnaf.sd.iam.api.realm;

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
    
    when(request.getRequestURI()).thenReturn("/api/one");
    assertThat(realmResolver.resolveRealmName(request), is("one"));
    
    when(request.getRequestURI()).thenReturn("/api/two");
    assertThat(realmResolver.resolveRealmName(request), is("two"));
    
    when(request.getRequestURI()).thenReturn("/api/three");
    assertThat(realmResolver.resolveRealmName(request), is("three"));
    
    when(request.getRequestURI()).thenReturn("/apione");
    assertThat(realmResolver.resolveRealmName(request), nullValue());
    
    when(request.getRequestURI()).thenReturn("/api/one/groups");
    assertThat(realmResolver.resolveRealmName(request), is("one"));
    
    when(request.getRequestURI()).thenReturn("/api/three/groups");
    assertThat(realmResolver.resolveRealmName(request), is("three"));
  }

}
