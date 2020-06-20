package it.infn.cnaf.sd.iam.api.service.notification;

@FunctionalInterface
public interface NotificationDeliveryService {
  int sendPendingNotifications();
}
