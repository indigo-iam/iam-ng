package it.infn.cnaf.sd.iam.api.service.notification;

import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;

public interface NotificationFactory {
  EmailNotificationEntity createRegistrationConfirmationMessage(RegistrationRequestEntity request);

  EmailNotificationEntity createHandleRegistrationMessage(RegistrationRequestEntity request);

  EmailNotificationEntity createRegistrationApprovedMessage(RegistrationRequestEntity request);

  EmailNotificationEntity createRegistrationRejectedMessage(RegistrationRequestEntity request);
}
