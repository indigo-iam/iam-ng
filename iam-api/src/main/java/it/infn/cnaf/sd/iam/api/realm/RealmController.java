package it.infn.cnaf.sd.iam.api.realm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.infn.cnaf.sd.iam.persistence.realm.entity.ConfigurationEntity;

@RestController
@RequestMapping(value = "/api/{realm}")
public class RealmController {

  public static final Logger LOG = LoggerFactory.getLogger(RealmController.class);

  private final RealmService service;

  public RealmController(RealmService service) {
    this.service = service;
  }

  @GetMapping("/config")
  RealmConfigDTO getRealm() {

    ConfigurationEntity ce = service.getRealmConfiguration();

    RealmConfigDTO dto = new RealmConfigDTO();

    dto.setName(ce.getName());
    dto.setCreationTime(ce.getMetadata().getCreationTime());
    dto.setLastUpdateTime(ce.getMetadata().getLastUpdateTime());
    return dto;

  }

}
