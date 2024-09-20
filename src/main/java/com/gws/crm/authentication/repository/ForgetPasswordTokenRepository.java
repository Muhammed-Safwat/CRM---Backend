package com.gws.crm.authentication.repository;

import com.gws.crm.authentication.entity.ForgetPasswordRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ForgetPasswordTokenRepository extends JpaRepository<ForgetPasswordRequest, UUID> {

    Optional<ForgetPasswordRequest> findByEmailAndCreatedAtAfter(String email, LocalDateTime createdAt);

    @Modifying
    @Transactional
    void deleteByEmail(String email);

    @Query("SELECT fbr FROM ForgetPasswordRequest fbr where fbr.token = :token AND fbr.valid = true")
    Optional<ForgetPasswordRequest> getByToken(@Param("token") String token);

    boolean existsByTokenAndValidAndExpiredDateAfter(String token, boolean valid, LocalDateTime expirationDate);
}
