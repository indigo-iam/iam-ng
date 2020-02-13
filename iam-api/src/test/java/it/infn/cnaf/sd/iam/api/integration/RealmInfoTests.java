package it.infn.cnaf.sd.iam.api.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.api.utils.ClockUtils;
import it.infn.cnaf.sd.iam.api.utils.MySqlIntegrationTest;
import it.infn.cnaf.sd.iam.api.utils.MySqlIntegrationTestSupport;

@RunWith(SpringRunner.class)
@IfProfileValue(name = "test-groups", values = {"mysql-integration"})
@MySqlIntegrationTest
@AutoConfigureMockMvc
public class RealmInfoTests extends MySqlIntegrationTestSupport implements ClockUtils {

  @Autowired
  MockMvc mvc;

  @Before
  public void setup() {
    RealmContext.clear();
  }

  @Test
  public void testUnauthenticatedAccess() throws Exception {
    mvc.perform(get("/api/one/config")).andExpect(status().isUnauthorized());
  }

  @Test
  public void testAuthenticatedAccess() throws Exception {

    Jwt jwt = Jwt.withTokenValue("token")
      .header("alg", "none")
      .claim("iss", "https://test")
      .claim("sub", "pippo")
      .build();

    mvc.perform(get("/api/one/config").with(jwt(jwt)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("one"));

    mvc.perform(get("/api/two/config").with(jwt(jwt)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("two"));
  }

}
