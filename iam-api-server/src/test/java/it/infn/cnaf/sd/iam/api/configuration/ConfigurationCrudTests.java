package it.infn.cnaf.sd.iam.api.configuration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.infn.cnaf.sd.iam.api.apis.configuration.dto.KcClient;
import it.infn.cnaf.sd.iam.api.apis.configuration.dto.RealmConfigurationDTO;
import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.IntegrationTestSupport;
import it.infn.cnaf.sd.iam.api.utils.WithMockAdminUser;

@RunWith(SpringRunner.class)
@IamTest
public class ConfigurationCrudTests extends IntegrationTestSupport {

  @Autowired
  ObjectMapper mapper;

  @Test
  public void testUnauthenticatedAccess() throws Exception {
    mvc.perform(get("/Realms/alice/Configuration")).andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  public void testUnauthorizedAccess() throws Exception {
    mvc.perform(get("/Realms/alice/Configuration")).andExpect(status().isForbidden());
  }

  @WithMockAdminUser
  @Test
  public void testAuthorizedAccess() throws Exception {
    mvc.perform(get("/Realms/alice/Configuration")).andExpect(status().isOk());
  }

  @WithMockAdminUser
  @Test
  public void testSaveConfiguration() throws Exception {
    RealmConfigurationDTO config = new RealmConfigurationDTO();
    KcClient kc = new KcClient();
    kc.setClientId("test");
    kc.setClientSecret("secret");

    config.setKc(kc);

    mvc.perform(put("/Realms/alice/Configuration").content(mapper.writeValueAsString(config))
      .contentType(APPLICATION_JSON)).andExpect(status().isOk());
    
    mvc.perform(get("/Realms/alice/Configuration")).andExpect(status().isOk())
      .andExpect(jsonPath("$.kc.clientId").value("test"))
      .andExpect(jsonPath("$.kc.clientSecret").value("secret"));
    
  }
  
  @WithMockAdminUser
  @Test
  public void testConfigValidation() throws Exception {
    RealmConfigurationDTO config = new RealmConfigurationDTO();
    KcClient kc = new KcClient();
    kc.setClientId(null);
    kc.setClientSecret(null);

    config.setKc(kc);

    mvc.perform(put("/Realms/alice/Configuration").content(mapper.writeValueAsString(config))
      .contentType(APPLICATION_JSON)).andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.error").value("bad_request"))
      .andExpect(jsonPath("$.errorDescription", containsString("Invalid configuration")));
    
  }
}
