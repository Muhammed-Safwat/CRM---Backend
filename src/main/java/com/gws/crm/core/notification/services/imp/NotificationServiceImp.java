package com.gws.crm.core.notification.services.imp;

import com.gws.crm.authentication.repository.UserRepository;
import com.gws.crm.common.entities.Transition;
import com.gws.crm.common.exception.NotFoundResourceException;
import com.gws.crm.core.notification.dtos.NotificationDTO;
import com.gws.crm.core.notification.dtos.RegistrationTokenReq;
import com.gws.crm.core.notification.entities.CrmNotification;
import com.gws.crm.core.notification.entities.NotificationToken;
import com.gws.crm.core.notification.mapper.NotificationMapper;
import com.gws.crm.core.notification.repository.NotificationRepository;
import com.gws.crm.core.notification.repository.NotificationTokenRepository;
import com.gws.crm.core.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.gws.crm.common.handler.ApiResponseHandler.*;
import static com.gws.crm.core.notification.enums.ClientType.fromString;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImp implements NotificationService {

    private final NotificationTokenRepository notificationTokenRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;


    @Override
    public ResponseEntity<?> registerToken(RegistrationTokenReq registrationTokenReq, Transition transition) {
       if(transition.getUserId()==null){
           return unauthorized("Authentication Error");
           }
        boolean isExists = notificationTokenRepository
                .existsByTokenAndUserId(registrationTokenReq.getToken(), transition.getUserId());
        if (isExists) {
            return success();
        }

        NotificationToken notificationToken =  NotificationToken.builder()
                .token(registrationTokenReq.getToken())
                .user(userRepository.findById(transition.getUserId())
                        .orElseThrow(NotFoundResourceException::new))
                .createdAt(LocalDateTime.now())
                .clientType(fromString(registrationTokenReq.getClientType()))
                .build();
        notificationTokenRepository.save(notificationToken);
        return success();
    }

    @Override
    public ResponseEntity<?> getAllNotification(int page, int size, Transition transition) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CrmNotification> notificationsPage = notificationRepository.findAllByRecipientId(transition.getUserId(),
                pageable);
        Page<NotificationDTO> notificationDTOSPage  =  NotificationMapper.toDto(notificationsPage);
        return success(notificationDTOSPage);
    }

    @Override
    public ResponseEntity<?> countClientNotification(Transition transition) {
        int count = notificationRepository.countByRecipientIdAndRead(transition.getUserId(),false);
        Map<String,Integer> notificationCountMap = new HashMap<>();
        notificationCountMap.put("count",count);
        return success(notificationCountMap);
    }

    @Override
    public ResponseEntity<?> sendNot() {
        return null;
    }

    @Override
    public ResponseEntity<?> markAllAsRead(Transition transition) {
        int count = notificationRepository.markAllAsRead(transition.getUserId());
        return success();
    }

    @Override
    public  ResponseEntity<?> markAsRead(long notificationId, Transition transition) {
        int count = notificationRepository.markAsReadByIdAndRecipientId(notificationId,transition.getUserId());
        return success();
    }

    @Override
    public ResponseEntity<?> deleteNotification(long notificationId, Transition transition) {
        int count =  notificationRepository.deleteByIdAndRecipientId(notificationId, transition.getUserId());
        log.info("========== {}",count);
        return success();
    }

}
