package com.jing.blogs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private JavaMailSender mailSender;
    @Value("${mail.fromMail.addr}")
    private String from;
    @Override
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
            logger.info("Mail Sent");
        } catch (Exception e){
            logger.error("Exception found in the process of sending",e);
        }
    }
}
