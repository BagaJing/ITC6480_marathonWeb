package com.jing.blogs.service;

public interface MailService {
    void sendEmail(String to,String subject,String content);
    void sendHtmlEmail(String to,String subject,String cotent);
}
