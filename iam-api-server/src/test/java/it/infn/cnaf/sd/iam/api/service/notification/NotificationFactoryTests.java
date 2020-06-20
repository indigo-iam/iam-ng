package it.infn.cnaf.sd.iam.api.service.notification;

import static it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity.DeliveryStatus.PENDING;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.com.google.common.collect.Lists;

import it.infn.cnaf.sd.iam.api.properties.IamProperties;
import it.infn.cnaf.sd.iam.api.service.notification.DefaultNotificationFactory.MessageType;
import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.IntegrationTestSupport;
import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RequesterInfoEntity;
import it.infn.cnaf.sd.iam.persistence.repository.EmailNotificationRepository;
import it.infn.cnaf.sd.iam.persistence.repository.RealmRepository;

@RunWith(SpringRunner.class)
@IamTest
public class NotificationFactoryTests extends IntegrationTestSupport {

  @Autowired
  NotificationFactory factory;

  @Mock
  RegistrationRequestEntity request;

  @Autowired
  RealmRepository realmRepo;

  @Autowired
  EmailNotificationRepository notificationRepo;

  @Mock
  RequesterInfoEntity requesterInfo;
  
  @Autowired
  IamProperties iamProperties;

  @Before
  public void setup() {

    RealmEntity realm =
        realmRepo.findByName("alice").orElseThrow(assertionError("Expected realm not found"));

    when(request.getRealm()).thenReturn(realm);
    when(request.getRequesterInfo()).thenReturn(requesterInfo);
    when(request.getEmailChallenge()).thenReturn("123");

    when(requesterInfo.getName()).thenReturn("Ciccio Paglia");
    when(requesterInfo.getEmail()).thenReturn("ciccio@example.io");
    when(requesterInfo.getUsername()).thenReturn("cicciopaglia");
  }

  @Test
  public void testConfirmNotificationCreation() {
    factory.createRegistrationConfirmationMessage(request);

    List<EmailNotificationEntity> emails = Lists.newArrayList(notificationRepo.findAll());

    assertThat(emails, hasSize(1));
    assertThat(emails.get(0).getStatus(), is(PENDING));
    assertThat(emails.get(0).getType(), is(MessageType.CONFIRM_REGISTRATION.name()));
    assertThat(emails.get(0).getRecipients(), hasSize(1));
    assertThat(emails.get(0).getRecipients(), hasItem("ciccio@example.io"));
    assertThat(emails.get(0).getSubject(), is("Confirm your registration request for alice"));
    assertThat(emails.get(0).getBody(),
        containsString("http://localhost:4200/alice/register/confirm/123"));
  }
  
  @Test
  public void testHandleNotificationCreation() {
    factory.createHandleRegistrationMessage(request);

    List<EmailNotificationEntity> emails = Lists.newArrayList(notificationRepo.findAll());

    assertThat(emails, hasSize(1));
    assertThat(emails.get(0).getStatus(), is(PENDING));
    assertThat(emails.get(0).getType(), is(MessageType.HANDLE_REGISTRATION.name()));
    assertThat(emails.get(0).getRecipients(), hasSize(1));
    assertThat(emails.get(0).getRecipients(), hasItem("admin@example.io"));
    assertThat(emails.get(0).getSubject(), is("New alice registration request"));
    assertThat(emails.get(0).getBody(),
        containsString("http://localhost:4200/alice/dashboard/requests/registration"));
  }

  @Test
  public void testRegistrationApprovedCreation() {
    factory.createRegistrationApprovedMessage(request);

    List<EmailNotificationEntity> emails = Lists.newArrayList(notificationRepo.findAll());
    assertThat(emails, hasSize(1));
    assertThat(emails.get(0).getStatus(), is(PENDING));
    assertThat(emails.get(0).getType(), is(MessageType.REGISTRATION_APPROVED.name()));
    assertThat(emails.get(0).getRecipients(), hasSize(1));
    assertThat(emails.get(0).getRecipients(), hasItem("ciccio@example.io"));
    assertThat(emails.get(0).getSubject(), is("Your alice registration request has been approved"));

    assertThat(emails.get(0).getBody(), containsString("cicciopaglia"));

  }

  @Test
  public void testRegistrationRejectedCreation() {
    factory.createRegistrationRejectedMessage(request);

    List<EmailNotificationEntity> emails = Lists.newArrayList(notificationRepo.findAll());
    assertThat(emails, hasSize(1));
    assertThat(emails.get(0).getStatus(), is(PENDING));
    assertThat(emails.get(0).getType(), is(MessageType.REGISTRATION_REJECTED.name()));
    assertThat(emails.get(0).getRecipients(), hasSize(1));
    assertThat(emails.get(0).getRecipients(), hasItem("ciccio@example.io"));
    assertThat(emails.get(0).getSubject(), is("Your alice registration request has been rejected"));

  }
}
