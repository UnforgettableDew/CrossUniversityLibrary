package com.crossuniversity.securityservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private HashMap<String, String> secretCodes = new HashMap<>();

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
        System.out.println(message.toString());
    }

    public HashMap<String, String> getSecretCodes() {
        return secretCodes;
    }

    public void addSecretCodes(String email, String secretCode) {
        this.secretCodes.put(email, secretCode);
    }
}

