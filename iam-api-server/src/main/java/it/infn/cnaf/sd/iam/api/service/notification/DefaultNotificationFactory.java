package it.infn.cnaf.sd.iam.api.service.notification;

import static com.google.common.collect.Lists.newArrayList;
import static it.infn.cnaf.sd.iam.api.service.notification.DefaultAddressResolutionService.VO_OWNERS;
import static it.infn.cnaf.sd.iam.api.service.notification.DefaultNotificationFactory.MessageType.CONFIRM_REGISTRATION;
import static it.infn.cnaf.sd.iam.api.service.notification.DefaultNotificationFactory.MessageType.HANDLE_REGISTRATION;
import static it.infn.cnaf.sd.iam.api.service.notification.DefaultNotificationFactory.MessageType.REGISTRATION_APPROVED;
import static it.infn.cnaf.sd.iam.api.service.notification.DefaultNotificationFactory.MessageType.REGISTRATION_REJECTED;

import java.io.IOException;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import it.infn.cnaf.sd.iam.api.properties.IamProperties;
import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity;
import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity.DeliveryStatus;
import it.infn.cnaf.sd.iam.persistence.entity.MetadataEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RealmEntity;
import it.infn.cnaf.sd.iam.persistence.entity.RegistrationRequestEntity;
import it.infn.cnaf.sd.iam.persistence.repository.EmailNotificationRepository;

@Service
public class DefaultNotificationFactory implements NotificationFactory {

  private static final String CONFIRM_REGISTRATION_MSG_TEMPLATE = "confirmRegistration.ftl";

  private static final String HANDLE_REGISTRATION_REQUEST_MSG_TEMPLATE = "handleRegistration.ftl";

  private static final String REGISTRATION_APPROVED_MSG_TEMPLATE =
      "registrationRequestApproved.ftl";

  private static final String REGISTRATION_REJECTED_MSG_TEMPLATE =
      "registrationRequestRejected.ftl";

  public enum MessageType {
    CONFIRM_REGISTRATION, HANDLE_REGISTRATION, REGISTRATION_APPROVED, REGISTRATION_REJECTED
  }

  public static final String DASHBOARD_CONFIRMATION_URL_TEMPLATE = "/%s/register/confirm/%s";

  public static final String DASHBOARD_MANAGE_REQUESTS_URL_TEMPLATE =
      "/%s/dashboard/requests/registration";

  private static final String RECIPIENT_FIELD = "recipient";
  private static final String ORGANISATION_NAME_FIELD = "organisationName";
  private static final String CONFIRMATION_URL_FIELD = "confirmationURL";
  private static final String HANDLE_REQUEST_URL_FIELD = "handleRequestURL";

  private static final String REQUESTER_FIELD = "requester";

  private static final String MESSAGE_CONFIRM_REGISTRATION_SUBJECT =
      "notification.confirmRegistration.subject";

  private static final String MESSAGE_HANDLE_REGISTRATION_SUBJECT =
      "notification.handleRegistration.subject";

  private static final String MESSAGE_REGISTRATION_APPROVED_SUBJECT =
      "notification.registrationApproved.subject";

  private static final String MESSAGE_REGISTRATION_REJECTED_SUBJECT =
      "notification.registrationRejected.subject";

  private final Configuration freemarkerConfig;
  private final Clock clock;
  private final IamProperties properties;
  private final MessageSource messageSource;
  private final EmailNotificationRepository repo;
  private final AddressResolutionService addressResolutionService;

  @Autowired
  public DefaultNotificationFactory(Clock clock, Configuration freemarkerConfig,
      IamProperties properties, MessageSource messageSource, EmailNotificationRepository repo,
      AddressResolutionService ars) {
    this.clock = clock;
    this.freemarkerConfig = freemarkerConfig;
    this.properties = properties;
    this.messageSource = messageSource;
    this.repo = repo;
    this.addressResolutionService = ars;
  }

  private String buildConfirmationUrl(RegistrationRequestEntity request) {

    final String confirmationUrl = String.format(DASHBOARD_CONFIRMATION_URL_TEMPLATE,
        request.getRealm().getName(), request.getEmailChallenge());

    return String.format("%s%s", properties.getDashboardBaseUrl(), confirmationUrl);
  }

  private String buildHandleRequestUrl(RegistrationRequestEntity request) {
    final String handleRequestsUrl =
        String.format(DASHBOARD_MANAGE_REQUESTS_URL_TEMPLATE, request.getRealm().getName());

    return String.format("%s%s", properties.getDashboardBaseUrl(), handleRequestsUrl);

  }

  @Override
  public EmailNotificationEntity createRegistrationConfirmationMessage(
      RegistrationRequestEntity request) {

    Map<String, Object> model = Maps.newHashMap();

    model.put(RECIPIENT_FIELD, request.getRequesterInfo().getName());
    model.put(CONFIRMATION_URL_FIELD, buildConfirmationUrl(request));
    model.put(ORGANISATION_NAME_FIELD, request.getRealm().getName());

    final String emailSubject = messageSource.getMessage(MESSAGE_CONFIRM_REGISTRATION_SUBJECT,
        new Object[] {request.getRealm().getName()}, LocaleContextHolder.getLocale());

    return createMessage(request.getRealm(), CONFIRM_REGISTRATION_MSG_TEMPLATE, model,
        CONFIRM_REGISTRATION, emailSubject, newArrayList(request.getRequesterInfo().getEmail()));
  }

  protected EmailNotificationEntity createMessage(RealmEntity realm, String templateFile,
      Map<String, Object> model, MessageType messageType, String subject, List<String> recipients) {

    try {
      Template template = freemarkerConfig.getTemplate(templateFile);
      String body = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

      EmailNotificationEntity entity = new EmailNotificationEntity();
      entity.setMetadata(MetadataEntity.fromCurrentInstant(clock));
      entity.setRealm(realm);
      entity.setUuid(UUID.randomUUID().toString());
      entity.setType(messageType.name());
      entity.setSubject(subject);
      entity.setBody(body);
      entity.setStatus(DeliveryStatus.PENDING);
      entity.setRecipients(recipients);

      repo.save(entity);

      return entity;

    } catch (IOException | TemplateException e) {
      throw new NotificationError(e.getMessage(), e);
    }
  }

  @Override
  public EmailNotificationEntity createHandleRegistrationMessage(
      RegistrationRequestEntity request) {

    Map<String, Object> model = Maps.newHashMap();
    model.put(ORGANISATION_NAME_FIELD, request.getRealm().getName());
    model.put(REQUESTER_FIELD, request.getRequesterInfo());
    model.put(HANDLE_REQUEST_URL_FIELD, buildHandleRequestUrl(request));

    final String emailSubject = messageSource.getMessage(MESSAGE_HANDLE_REGISTRATION_SUBJECT,
        new Object[] {request.getRealm().getName()}, LocaleContextHolder.getLocale());

    return createMessage(request.getRealm(), HANDLE_REGISTRATION_REQUEST_MSG_TEMPLATE, model,
        HANDLE_REGISTRATION, emailSubject,
        addressResolutionService.resolveAddressesForAudience(VO_OWNERS));
  }

  @Override
  public EmailNotificationEntity createRegistrationApprovedMessage(
      RegistrationRequestEntity request) {

    Map<String, Object> model = Maps.newHashMap();
    model.put(ORGANISATION_NAME_FIELD, request.getRealm().getName());
    model.put(REQUESTER_FIELD, request.getRequesterInfo());

    final String emailSubject = messageSource.getMessage(MESSAGE_REGISTRATION_APPROVED_SUBJECT,
        new Object[] {request.getRealm().getName()}, LocaleContextHolder.getLocale());

    return createMessage(request.getRealm(), REGISTRATION_APPROVED_MSG_TEMPLATE, model,
        REGISTRATION_APPROVED, emailSubject, newArrayList(request.getRequesterInfo().getEmail()));
  }

  @Override
  public EmailNotificationEntity createRegistrationRejectedMessage(
      RegistrationRequestEntity request) {

    Map<String, Object> model = Maps.newHashMap();
    model.put(ORGANISATION_NAME_FIELD, request.getRealm().getName());
    model.put(REQUESTER_FIELD, request.getRequesterInfo());

    final String emailSubject = messageSource.getMessage(MESSAGE_REGISTRATION_REJECTED_SUBJECT,
        new Object[] {request.getRealm().getName()}, LocaleContextHolder.getLocale());

    return createMessage(request.getRealm(), REGISTRATION_REJECTED_MSG_TEMPLATE, model,
        REGISTRATION_REJECTED, emailSubject, newArrayList(request.getRequesterInfo().getEmail()));
  }
}
