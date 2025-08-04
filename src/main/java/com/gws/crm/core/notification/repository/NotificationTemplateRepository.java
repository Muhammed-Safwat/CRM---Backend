package com.gws.crm.core.notification.repository;

import com.gws.crm.core.notification.entities.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    Optional<NotificationTemplate> findByCodeAndLanguage(String code, String language);

    Optional<NotificationTemplate> findByCode(String code);

}