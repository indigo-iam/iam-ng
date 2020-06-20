package it.infn.cnaf.sd.iam.api.service.notification;

import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "iam.notification.disable", havingValue = "true")
@Primary
public class NoOpMailSender implements JavaMailSender {

  public NoOpMailSender() {
    // empty on purpose
  }

  @Override
  public void send(SimpleMailMessage simpleMessage) throws MailException {
    // empty on purpose
  }

  @Override
  public void send(SimpleMailMessage... simpleMessages) throws MailException {
    // empty on purpose
  }

  @Override
  public MimeMessage createMimeMessage() {
    // empty on purpose
    return null;
  }

  @Override
  public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
    // empty on purpose
    return null;
  }

  @Override
  public void send(MimeMessage mimeMessage) throws MailException {
    // empty on purpose
  }

  @Override
  public void send(MimeMessage... mimeMessages) throws MailException {
    // empty on purpose
  }

  @Override
  public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
    // empty on purpose
  }

  @Override
  public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
    // empty on purpose
  }

}
