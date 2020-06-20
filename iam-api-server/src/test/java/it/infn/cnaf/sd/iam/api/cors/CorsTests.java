package it.infn.cnaf.sd.iam.api.cors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;

import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.IntegrationTestSupport;

@RunWith(SpringRunner.class)
@IamTest
public class CorsTests extends IntegrationTestSupport {


  @Test
  @WithAnonymousUser
  public void testCorsConfig() throws Exception {
    
    mvc.perform(options("/Realms")
        .header("Access-Control-Request-Method", "GET")
        .header("Origin", "www.example.org"))
      .andExpect(status().isOk())
      .andExpect(header().exists("Vary"))
      .andExpect(header().exists("Access-Control-Allow-Origin"))
      .andExpect(header().exists("Access-Control-Allow-Methods"));
    
    mvc.perform(options("/Realms/alice/Configurations")
        .header("Access-Control-Request-Method", "GET")
        .header("Origin", "www.example.org"))
      .andExpect(status().isOk())
      .andExpect(header().exists("Vary"))
      .andExpect(header().exists("Access-Control-Allow-Origin"))
      .andExpect(header().exists("Access-Control-Allow-Methods"));
  }

}
