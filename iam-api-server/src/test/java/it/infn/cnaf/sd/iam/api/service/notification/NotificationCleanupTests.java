package it.infn.cnaf.sd.iam.api.service.notification;

import static it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity.DeliveryStatus.DELIVERED;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;

import java.time.Clock;
import java.time.Duration;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.IntegrationTestSupport;
import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity;
import it.infn.cnaf.sd.iam.persistence.entity.MetadataEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;
import it.infn.cnaf.sd.iam.persistence.repository.EmailNotificationRepository;
import it.infn.cnaf.sd.iam.persistence.repository.RealmRepository;

@RunWith(SpringRunner.class)
@IamTest
public class NotificationCleanupTests extends IntegrationTestSupport {
  
  public static final int NOTIFICATION_COUNT = 5;

  @Autowired
  RealmRepository realmRepo;

  @Autowired
  EmailNotificationRepository notificationRepo;

  @Autowired
  NotificationCleanupService cleanupService;

  @Autowired
  Clock clock;

  @Test
  public void testCleanup() {
    Clock tenDaysAgo = Clock.offset(clock, Duration.ofDays(-10));
    Clock fiveDaysAgo = Clock.offset(clock, Duration.ofDays(-5));

    RealmEntity realm =
        realmRepo.findByName("alice").orElseThrow(assertionError("Expected realm not found"));

    for (int i = 0; i < NOTIFICATION_COUNT*2; i++) {
      EmailNotificationEntity testEntity = new EmailNotificationEntity();
      
      if (i < NOTIFICATION_COUNT) {
        testEntity.setMetadata(MetadataEntity.fromCurrentInstant(tenDaysAgo));
      } else {
        testEntity.setMetadata(MetadataEntity.fromCurrentInstant(fiveDaysAgo));
      }
      
      testEntity.setRealm(realm);
      testEntity.setType("test");
      testEntity.setUuid(UUID.randomUUID().toString());
      testEntity.setSubject(Integer.toString(i));
      testEntity.setBody("Test " + i);
      testEntity.setStatus(DELIVERED);
      testEntity.setRecipients(newArrayList("test@example.io"));
      notificationRepo.save(testEntity);
    }
    
    int cleanupCount = cleanupService.cleanupDeliveredNotifications();
    Assert.assertThat(cleanupCount, is(NOTIFICATION_COUNT));
  }

}
