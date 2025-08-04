package com.gws.crm.core.notification.repository;

import com.gws.crm.core.notification.entities.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {

    Optional<NotificationType> findByCode(String code);
}
