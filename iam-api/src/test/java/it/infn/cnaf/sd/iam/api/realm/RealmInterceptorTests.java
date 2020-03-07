package it.infn.cnaf.sd.iam.api.realm;

import static it.infn.cnaf.sd.iam.api.common.realm.RealmInterceptor.REALM_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import it.infn.cnaf.sd.iam.api.common.error.BadRequestError;
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


  @Test(expected = BadRequestError.class)
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
    
    assertThat(RealmContext.getCurrentRealm(), is("test"));

    verify(request).setAttribute(Mockito.eq(REALM_KEY), Mockito.eq("test"));

    interceptor.afterCompletion(request, response, testRealm, exception);

    assertThat(RealmContext.getCurrentRealm(), nullValue());

  }

}
