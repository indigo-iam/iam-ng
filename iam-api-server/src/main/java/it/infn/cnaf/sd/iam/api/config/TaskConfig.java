package it.infn.cnaf.sd.iam.api.config;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import it.infn.cnaf.sd.iam.api.properties.IamProperties;
import it.infn.cnaf.sd.iam.api.service.notification.NotificationDeliveryService;

@Configuration
@EnableScheduling
public class TaskConfig implements SchedulingConfigurer {

  public static final Logger LOG = LoggerFactory.getLogger(TaskConfig.class);

  @Autowired
  IamProperties iamProperties;

  @Autowired
  NotificationDeliveryService nds;

  public void schedulePendingNotificationsDelivery(final ScheduledTaskRegistrar taskRegistrar) {

    if (iamProperties.getNotification().isDisable()) {
      LOG.info("Notification delivery task is DISABLED");
    } else {
      LOG.info("Scheduling notification delivery service to run every {} sec",
          iamProperties.getNotification().getDeliveryTaskPeriodSeconds());

      taskRegistrar.addFixedRateTask(() -> nds.sendPendingNotifications(), TimeUnit.SECONDS
        .toMillis(iamProperties.getNotification().getDeliveryTaskPeriodSeconds()));
    }
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    schedulePendingNotificationsDelivery(taskRegistrar);
  }
}
