package com.gws.crm.core.notification.repository;

import com.gws.crm.core.notification.entities.NotificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {

    Optional<NotificationToken> findByTokenAndUserId(String Token, Long userId);

    List<NotificationToken> findByUserId(Long userId);

    boolean existsByTokenAndUserId(String token, Long userId);

}
