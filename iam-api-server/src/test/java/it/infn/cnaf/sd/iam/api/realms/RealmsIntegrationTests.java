package it.infn.cnaf.sd.iam.api.realms;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;

import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.IntegrationTestSupport;

@RunWith(SpringRunner.class)
@IamTest
@WithAnonymousUser
public class RealmsIntegrationTests extends IntegrationTestSupport{

  @Test
  public void testUnauthenticatedGetRealmsWorks() throws Exception {
    
    mvc.perform(get("/Realms")).andExpect(status().isOk());
   
  }
}
