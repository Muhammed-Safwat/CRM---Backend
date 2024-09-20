package com.gws.crm.common.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendRestPasswordEmail(String email, String link) throws MessagingException;
}
