package it.infn.cnaf.sd.iam.api.service.notification;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import it.infn.cnaf.sd.iam.api.properties.IamProperties;

@RunWith(MockitoJUnitRunner.class)
public class AddressResolutionServiceTests {

  public static final String ADMIN_EMAIL_ADDRESS = "admin@example.org";

  IamProperties properties;
  DefaultAddressResolutionService service;

  @Before
  public void setup() {
    properties = new IamProperties();
    properties.getRequests().setAdminEmailAddress(ADMIN_EMAIL_ADDRESS);
    service = new DefaultAddressResolutionService(properties);
  }

  @Test
  public void testVoAdminsResolvesToAdminAddress() {
    List<String> adminAddresses = service.resolveAddressesForAudience("vo-admins");

    assertThat(adminAddresses, hasSize(1));
    assertThat(adminAddresses, hasItem(ADMIN_EMAIL_ADDRESS));

  }

  @Test
  public void testVoOwnersResolvesToAdminAddress() {
    List<String> adminAddresses = service.resolveAddressesForAudience("vo-owners");
    assertThat(adminAddresses, hasSize(1));
    assertThat(adminAddresses, hasItem(ADMIN_EMAIL_ADDRESS));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnknownAudienceRaisesException() {
    DefaultAddressResolutionService service = new DefaultAddressResolutionService(properties);
    
    try {
      service.resolveAddressesForAudience("unknown-audience");
    } catch (IllegalArgumentException e) {
      
      assertThat(e.getMessage(), containsString("Unknown audience: unknown-audience"));
      throw e;
    }
    

  }



}
