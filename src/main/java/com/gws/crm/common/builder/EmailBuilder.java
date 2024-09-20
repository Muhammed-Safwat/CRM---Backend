package com.gws.crm.common.builder;

import jakarta.mail.internet.MimeMessage;

public abstract class EmailBuilder {

    protected MimeMessage mimeMessage;

    public EmailBuilder(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

}
