package it.infn.cnaf.sd.iam.api.service.notification;

import static it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity.DeliveryStatus.PENDING;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.Clock;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.IntegrationTestSupport;
import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity;
import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity.DeliveryStatus;
import it.infn.cnaf.sd.iam.persistence.entity.MetadataEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;
import it.infn.cnaf.sd.iam.persistence.repository.EmailNotificationRepository;
import it.infn.cnaf.sd.iam.persistence.repository.RealmRepository;

@RunWith(SpringRunner.class)
@IamTest
@TestPropertySource(properties = "iam.notification.disable=true")
public class NotificationDeliveryTests extends IntegrationTestSupport {

  public static final int NOTIFICATION_COUNT = 20;

  @Autowired
  RealmRepository realmRepo;

  @Autowired
  EmailNotificationRepository notificationRepo;

  @Autowired
  NotificationDeliveryService deliveryService;


  @MockBean
  JavaMailSender mailSender;

  @Before
  public void setup() {

    RealmEntity realm =
        realmRepo.findByName("alice").orElseThrow(assertionError("Expected realm not found"));

    for (int i = 0; i < NOTIFICATION_COUNT; i++) {
      EmailNotificationEntity testEntity = new EmailNotificationEntity();
      testEntity.setMetadata(MetadataEntity.fromCurrentInstant(Clock.systemDefaultZone()));
      testEntity.setRealm(realm);
      testEntity.setType("test");
      testEntity.setUuid(UUID.randomUUID().toString());
      testEntity.setSubject(Integer.toString(i));
      testEntity.setBody("Test " + i);
      testEntity.setStatus(PENDING);
      testEntity.setRecipients(newArrayList("test@example.io"));
      notificationRepo.save(testEntity);
    }
  }

  @Test
  public void testDelivery() {
    int deliveryCount = deliveryService.sendPendingNotifications();

    Page<EmailNotificationEntity> page =
        notificationRepo.findByStatus(DeliveryStatus.PENDING, Pageable.unpaged());

    assertThat(page.getTotalElements(), is(0L));
    assertThat(deliveryCount, is(NOTIFICATION_COUNT));
  }
  
  @Test
  public void testPartialDeliveryError() {
    Mockito.doAnswer(i -> {
      SimpleMailMessage m = (SimpleMailMessage) i.getArgument(0);
      if (Integer.parseInt(m.getSubject()) % 2 == 0) {
        throw new MailSendException("Error!");
      }
      return null;
    }).when(mailSender).send(Mockito.any(SimpleMailMessage.class));

    int deliveryCount = deliveryService.sendPendingNotifications();

    Page<EmailNotificationEntity> page =
        notificationRepo.findByStatus(DeliveryStatus.DELIVERY_ERROR, Pageable.unpaged());

    assertThat(page.getTotalElements(), is((long)NOTIFICATION_COUNT/2));
    assertThat(deliveryCount, is(NOTIFICATION_COUNT/2));
  }

}
