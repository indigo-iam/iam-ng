package it.infn.cnaf.sd.iam.api.service.notification;

import java.util.List;

@FunctionalInterface
public interface AddressResolutionService {

  List<String> resolveAddressesForAudience(String name);
}
