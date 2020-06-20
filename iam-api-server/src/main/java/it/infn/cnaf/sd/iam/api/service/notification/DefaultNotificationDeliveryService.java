package it.infn.cnaf.sd.iam.api.service.notification;

import static it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity.DeliveryStatus.DELIVERED;
import static it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity.DeliveryStatus.DELIVERY_ERROR;
import static java.util.stream.Collectors.summingInt;

import java.time.Clock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.infn.cnaf.sd.iam.api.common.utils.PageUtils;
import it.infn.cnaf.sd.iam.api.properties.IamProperties;
import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity;
import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity.DeliveryStatus;
import it.infn.cnaf.sd.iam.persistence.repository.EmailNotificationRepository;


@Service
@Transactional
public class DefaultNotificationDeliveryService implements NotificationDeliveryService {

  public static final Logger LOG =
      LoggerFactory.getLogger(DefaultNotificationDeliveryService.class);

  public static final int PAGE_SIZE = 50;

  protected final EmailNotificationRepository repo;
  protected final IamProperties properties;
  protected final JavaMailSender mailSender;
  protected final Clock clock;

  @Autowired
  public DefaultNotificationDeliveryService(Clock clock, EmailNotificationRepository repo,
      JavaMailSender mailSender, IamProperties properties) {
    this.clock = clock;
    this.repo = repo;
    this.mailSender = mailSender;
    this.properties = properties;

  }

  protected SimpleMailMessage messageFromNotification(EmailNotificationEntity notification) {
    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom(properties.getNotification().getFrom());
    message.setSubject(notification.getSubject());
    message.setText(notification.getBody());
    message.setTo(notification.getRecipients().stream().toArray(String[]::new));

    return message;
  }

  protected void logNotificationDelivered(EmailNotificationEntity notification) {
    LOG.info("Email notification delivered [id: {}, subject: {}, recipients: {}, body: {}",
        notification.getUuid(), notification.getSubject(), notification.getRecipients(),
        notification.getBody());
  }

  protected boolean sendNotification(EmailNotificationEntity notification) {

    try {
      SimpleMailMessage message = messageFromNotification(notification);
      mailSender.send(message);
      notification.setStatus(DELIVERED);
      logNotificationDelivered(notification);
    } catch (MailException e) {
      notification.setStatus(DELIVERY_ERROR);
      LOG.error("Error delivering notification {}: {}", notification.getUuid(), e.getMessage());
      if (LOG.isDebugEnabled()) {
        LOG.error(e.getMessage(), e);
      }

    }

    notification.getMetadata().touch(clock);
    repo.save(notification);

    return notification.getStatus().equals(DELIVERED);
  }

  @Override
  public int sendPendingNotifications() {

    int deliveryCount = 0;

    deliveryCount = repo
      .findByStatus(DeliveryStatus.PENDING, PageUtils.buildPageRequest(PAGE_SIZE, 0, PAGE_SIZE))
      .stream()
      .map(this::sendNotification)
      .collect(summingInt(b -> b ? 1 : 0));

    return deliveryCount;
  }
}
