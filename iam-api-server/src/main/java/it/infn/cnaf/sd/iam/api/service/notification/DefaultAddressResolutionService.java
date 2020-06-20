package it.infn.cnaf.sd.iam.api.service.notification;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import it.infn.cnaf.sd.iam.api.properties.IamProperties;

@Service
public class DefaultAddressResolutionService implements AddressResolutionService {

  public static final String VO_ADMINS = "vo-admins";
  public static final String VO_OWNERS = "vo-owners";

  private final List<String> ADMIN_ADDRESS;

  public DefaultAddressResolutionService(IamProperties iamProperties) {
    ADMIN_ADDRESS = Lists.newArrayList(iamProperties.getRequests().getAdminEmailAddress());
  }

  @Override
  public List<String> resolveAddressesForAudience(String name) {

    switch (name) {
      case VO_ADMINS:
      case VO_OWNERS:
        return ADMIN_ADDRESS;
      default:
        throw new IllegalArgumentException("Unknown audience: " + name);
    }

  }

}
