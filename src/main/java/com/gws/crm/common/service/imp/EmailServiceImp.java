package com.gws.crm.common.service.imp;

import com.gws.crm.common.builder.imp.ResetPasswordEmailBuilder;
import com.gws.crm.common.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImp implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendRestPasswordEmail(String email, String link) throws MessagingException {
        ResetPasswordEmailBuilder emailBuilder = new ResetPasswordEmailBuilder(mailSender.createMimeMessage());
        mailSender.send(emailBuilder.createEmail(email, link));
    }
}
