package it.infn.cnaf.sd.iam.api.groups;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.infn.cnaf.sd.iam.api.apis.group.dto.GroupDTO;
import it.infn.cnaf.sd.iam.api.apis.group.dto.GroupRefDTO;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.WithMockAdminUser;

@RunWith(SpringRunner.class)
@IamTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class GroupsTests {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper mapper;

  @Before
  public void setup() {
    RealmContext.clear();
  }

  @Test
  public void testUnauthenticatedAccess() throws Exception {
    mvc.perform(get("/api/alice/Groups")).andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  public void testUnauthorizedAccess() throws Exception {
    mvc.perform(get("/api/alice/Groups")).andExpect(status().isForbidden());
  }

  @WithMockAdminUser
  @Test
  public void testAuthorizedAccess() throws Exception {
    mvc.perform(get("/api/alice/Groups")).andExpect(status().isOk());
  }

  @WithMockAdminUser
  @Test
  public void testUnknownRealmAccess() throws Exception {
    mvc.perform(get("/api/unknown/Groups"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.error").value("not_found"))
      .andExpect(jsonPath("$.error_description").value("Unknown realm: unknown"));
  }

  @WithMockAdminUser
  @Test
  public void testChildrenGroupCreationSuccess() throws Exception {

    GroupDTO group = new GroupDTO();
    group.setName("testing");
    GroupRefDTO parentRef = new GroupRefDTO();
    parentRef.setName("alice");

    group.setParentGroup(parentRef);
    group.setDescription("Yo yo brotha");

    mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.name").value("alice/testing"))
      .andExpect(jsonPath("$.parent_group.name").value("alice"))
      .andExpect(jsonPath("$.metadata.creation_time").exists())
      .andExpect(jsonPath("$.metadata.last_update_time").exists())
      .andExpect(jsonPath("$.uuid").exists())
      .andExpect(jsonPath("$.id").exists());
  }
  
  @WithMockAdminUser
  @Test
  public void testRootGroupCreationSuccess() throws Exception {

    GroupDTO group = new GroupDTO();
    group.setName("testing");
    group.setDescription("Yo yo brotha");

    mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.name").value("testing"))
      .andExpect(jsonPath("$.parent_group").doesNotExist())
      .andExpect(jsonPath("$.metadata.creation_time").exists())
      .andExpect(jsonPath("$.metadata.last_update_time").exists())
      .andExpect(jsonPath("$.uuid").exists())
      .andExpect(jsonPath("$.id").exists());
  }

}
