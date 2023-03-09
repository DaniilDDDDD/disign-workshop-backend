package com.workshop.backgroundservice.service.email;

import org.springframework.mail.MailException;

import javax.mail.MessagingException;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

    void sendEmail(
            String to,
            String subject,
            String text,
            String pathToAttachment
    ) throws MailException, MessagingException;

}
