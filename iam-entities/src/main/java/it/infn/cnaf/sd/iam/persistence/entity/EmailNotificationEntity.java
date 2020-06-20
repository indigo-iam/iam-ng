package it.infn.cnaf.sd.iam.persistence.entity;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EMAIL_NOTIFICATIONS")
public class EmailNotificationEntity implements Serializable {

  public enum DeliveryStatus {
    PENDING,
    DELIVERED,
    DELIVERY_ERROR
  }

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "UUID", nullable = false, length = 36, unique = true)
  private String uuid;

  private MetadataEntity metadata;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REALM_ID", nullable = false)
  private RealmEntity realm;

  @Column(name = "TYPE", length = 32, nullable = false)
  private String type;

  @Column(name = "SUBJECT", length = 128, nullable = false)
  private String subject;

  @Lob
  @Column(name = "BODY", nullable = false)
  private String body;

  @Column(name = "STATUS", nullable = false)
  @Enumerated(EnumType.STRING)
  private DeliveryStatus status;

  @ElementCollection
  @Column(name = "RECIPIENT_ADDR", length = 255)
  @CollectionTable(name = "EMAIL_NOTIFICATIONS_RECIPIENTS",
      joinColumns = @JoinColumn(name = "NOTIFICATION_ID"),
      indexes = {@Index(columnList = "NOTIFICATION_ID,RECIPIENT_ADDR", unique = true),
          @Index(columnList = "RECIPIENT_ADDR", unique = false)})
  private List<String> recipients;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public DeliveryStatus getStatus() {
    return status;
  }

  public void setStatus(DeliveryStatus status) {
    this.status = status;
  }

  public List<String> getRecipients() {
    return recipients;
  }

  public void setRecipients(List<String> recipients) {
    this.recipients = recipients;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public RealmEntity getRealm() {
    return realm;
  }

  public void setRealm(RealmEntity realm) {
    this.realm = realm;
  }

  public MetadataEntity getMetadata() {
    return metadata;
  }

  public void setMetadata(MetadataEntity metadata) {
    this.metadata = metadata;
  }

  @Override
  @Generated("eclipse")
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    return result;
  }

  @Override
  @Generated("eclipse")
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EmailNotificationEntity other = (EmailNotificationEntity) obj;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals(other.uuid))
      return false;
    return true;
  }



}
