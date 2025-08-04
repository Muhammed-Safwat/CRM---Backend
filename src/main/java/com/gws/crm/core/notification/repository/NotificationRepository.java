package com.gws.crm.core.notification.repository;

import com.gws.crm.core.notification.entities.CrmNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<CrmNotification, Long> {

    Page<CrmNotification> findAllByRecipientId(long id, Pageable pageable);

    @Modifying
    @Transactional
    int deleteByIdAndRecipientId(long notificationId,long recipientId);

    int countByRecipientIdAndRead(Long userId,boolean read);

    @Modifying
    @Transactional
    @Query("UPDATE CrmNotification n SET n.read = true, n.readAt = CURRENT_TIMESTAMP WHERE n.id = :id AND n.recipientId = :recipientId")
    int markAsReadByIdAndRecipientId(@Param("id") long id, @Param("recipientId") long recipientId);

    @Modifying
    @Transactional
    @Query("UPDATE CrmNotification n SET n.read = true, n.readAt = CURRENT_TIMESTAMP WHERE n.recipientId = :userId AND n.read = false")
    int markAllAsRead(@Param("userId") Long userId);
}
