package it.infn.cnaf.sd.iam.api.service.notification;

import static it.infn.cnaf.sd.iam.api.common.utils.PageUtils.buildPageRequest;

import java.time.Clock;
import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import it.infn.cnaf.sd.iam.api.properties.IamProperties;
import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity;
import it.infn.cnaf.sd.iam.persistence.repository.EmailNotificationRepository;

@Service
public class DefaultNotificationCleanupService implements NotificationCleanupService {

  public static final int PAGE_SIZE = 100;

  private final Clock clock;
  private final IamProperties properties;
  private final EmailNotificationRepository repo;

  @Autowired
  public DefaultNotificationCleanupService(Clock clock, IamProperties properties,
      EmailNotificationRepository repo) {

    this.clock = clock;
    this.properties = properties;
    this.repo = repo;
  }


  @Override
  public int cleanupDeliveredNotifications() {
    int retentionPeriodDays =
        properties.getNotification().getDeliveredNotificationsRetentionPeriodDays();

    Date expirationDate = Date.from(clock.instant().minus(Duration.ofDays(retentionPeriodDays)));

    Page<EmailNotificationEntity> page =
        repo.findExpired(expirationDate, buildPageRequest(PAGE_SIZE));

    page.forEach(repo::delete);

    return page.getNumberOfElements();
  }

}
