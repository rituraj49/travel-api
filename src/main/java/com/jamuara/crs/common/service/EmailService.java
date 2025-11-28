package com.jamuara.crs.common.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmailService {
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    TemplateEngine templateEngine;

    public void sendEmail(String receiverEmail, String subject, String htmlBody, Map<String, byte[]> attachments)
            throws MessagingException {
        log.info("sending email to {}: ", receiverEmail);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(receiverEmail);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        for(Map.Entry<String, byte[]> entry: attachments.entrySet()) {
            String filename = entry.getKey() + ".pdf";
            byte[] data = entry.getValue();

            helper.addAttachment(filename, new ByteArrayDataSource(data, "application/pdf"));
        }

        mailSender.send(message);
    }
}
