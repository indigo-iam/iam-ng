package it.infn.cnaf.sd.iam.persistence.repository;

import java.util.Date;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity;
import it.infn.cnaf.sd.iam.persistence.entity.EmailNotificationEntity.DeliveryStatus;

public interface EmailNotificationRepository
    extends PagingAndSortingRepository<EmailNotificationEntity, Long> {

  Page<EmailNotificationEntity> findByRealmName(String realmName, Pageable page);

  Page<EmailNotificationEntity> findByRealmNameAndStatus(String realmName, DeliveryStatus status,
      Pageable page);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Page<EmailNotificationEntity> findByStatus(DeliveryStatus status, Pageable page);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select e from EmailNotificationEntity e where e.status = 'DELIVERED' and e.metadata.lastUpdateTime < :timestamp")
  Page<EmailNotificationEntity> findExpired(@Param("timestamp") Date timestamp, Pageable page);


}
