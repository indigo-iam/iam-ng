package it.infn.cnaf.sd.iam.api.groups;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.infn.cnaf.sd.iam.api.apis.group.GroupService;
import it.infn.cnaf.sd.iam.api.apis.group.dto.GroupDTO;
import it.infn.cnaf.sd.iam.api.apis.group.dto.GroupRefDTO;
import it.infn.cnaf.sd.iam.api.common.realm.RealmContext;
import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.WithMockAdminUser;

@RunWith(SpringRunner.class)
@IamTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
public class GroupsTests {

  public static final String ALICE_GROUP_UUID = "c6fe138f-2f55-4d1d-b2b5-968bdf3bdff4";
  public static final String ALICE_PRODUCTION_GROUP_UUID = "4b98e830-6e46-4051-b492-4f970d8c9260";

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper mapper;

  @Autowired
  GroupService groupService;

  @Autowired
  EntityManager em;

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
      .andExpect(jsonPath("$.errorDescription").value("Unknown realm: unknown"));
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
      .andExpect(jsonPath("$.parentGroup.name").value("alice"))
      .andExpect(jsonPath("$.metadata.creationTime").exists())
      .andExpect(jsonPath("$.metadata.lastUpdateTime").exists())
      .andExpect(jsonPath("$.uuid").exists())
      .andExpect(jsonPath("$.id").exists())
      .andExpect(jsonPath("$.realm.name").value("alice"));
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
      .andExpect(jsonPath("$.parentGroup").doesNotExist())
      .andExpect(jsonPath("$.metadata.creationTime").exists())
      .andExpect(jsonPath("$.metadata.lastUpdateTime").exists())
      .andExpect(jsonPath("$.uuid").exists())
      .andExpect(jsonPath("$.id").exists())
      .andExpect(jsonPath("$.realm.name").value("alice"));
  }

  @WithMockAdminUser
  @Test
  public void testGroupValidation() throws Exception {

    // Empty group name
    GroupDTO group = new GroupDTO();

    mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errorDescription", containsString("group name cannot be blank")));

    // Invalid Group name
    group = new GroupDTO();
    group.setName("test/with/slashes");;

    mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errorDescription", containsString("does not match with regexp")));

    // Group name too long
    group = new GroupDTO();
    group.setName(RandomStringUtils.randomAlphabetic(513));

    mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errorDescription", containsString("group name cannot be longer")));
    
    // Group name plus parent too long
    group = new GroupDTO();
    group.setName(RandomStringUtils.randomAlphabetic(508));
    
    GroupRefDTO parentRef = new GroupRefDTO();
    parentRef.setName("alice");
    group.setParentGroup(parentRef);

    mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errorDescription", containsString("composite group name exceeds")));

    // Invalid group description
    group = new GroupDTO();
    group.setName("test");
    group.setDescription("<script>whatever</script>");

    mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errorDescription", containsString("does not match with regexp")));

    // Group description too long
    group = new GroupDTO();
    group.setName("test");
    group.setDescription(RandomStringUtils.randomAlphabetic(513));

    mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(
          jsonPath("$.errorDescription", containsString("group description cannot be longer")));

    // null parent ref
    group = new GroupDTO();
    group.setName("testing");
    parentRef = new GroupRefDTO();
    group.setParentGroup(parentRef);

    mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errorDescription", containsString("group ref name cannot be blank")));

    // invalid parent ref
    group = new GroupDTO();
    group.setName("testing");
    parentRef = new GroupRefDTO();
    parentRef.setName("unknown");
    group.setParentGroup(parentRef);

    mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errorDescription", containsString("Invalid group name")));

  }

  @WithMockAdminUser
  @Test
  public void testGetGroupSuccess() throws Exception {
    mvc.perform(get("/api/alice/Groups/{id}", ALICE_PRODUCTION_GROUP_UUID))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("alice/production"))
      .andExpect(jsonPath("$.realm.name").value("alice"));

  }

  @WithMockAdminUser
  @Test
  public void testGroupDeletion() throws Exception {
    GroupDTO group = new GroupDTO();
    group.setName("test");

    String createdGroupRepr = mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn()
      .getResponse()
      .getContentAsString();

    GroupDTO createdGroup = mapper.readValue(createdGroupRepr, GroupDTO.class);

    mvc.perform(get("/api/alice/Groups/{id}", createdGroup.getUuid()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("test"));

    mvc.perform(delete("/api/alice/Groups/{id}", createdGroup.getUuid()))
      .andExpect(status().isNoContent());

    mvc.perform(get("/api/alice/Groups/{id}", createdGroup.getUuid()))
      .andExpect(status().isNotFound())
      .andExpect(
          jsonPath("$.errorDescription").value("Group not found: " + createdGroup.getUuid()));

    mvc.perform(delete("/api/alice/Groups/{id}", createdGroup.getUuid()))
      .andExpect(status().isNotFound())
      .andExpect(
          jsonPath("$.errorDescription").value("Group not found: " + createdGroup.getUuid()));
  }

  @WithMockAdminUser
  @Test
  public void testGroupDeletionFailsForGroupWithChildren() throws Exception {
    mvc.perform(delete("/api/alice/Groups/{id}", ALICE_GROUP_UUID))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errorDescription", containsString("has children")));
  }

  @WithMockUser
  @Test
  public void testGroupDeletionRequiresOwnerPrivileges() throws Exception {
    mvc.perform(get("/api/alice/Groups/{id}", ALICE_GROUP_UUID)).andExpect(status().isForbidden());
  }

  @WithMockUser
  @Test
  public void testGroupCreationRequiresOwnerPrivileges() throws Exception {
    GroupDTO group = new GroupDTO();
    group.setName("test");

    mvc
      .perform(post("/api/alice/Groups").content(mapper.writeValueAsString(group))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

}
