package com.Hindol.SpringSecurity.Model;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class UserListener {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private String sender;
    @PostPersist
    public void prePersist(Object obj) {
        if(obj instanceof User) {
            User user = (User)obj;
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(user.getEmail());
            simpleMailMessage.setText("Welcome " + user.getFirstName() + user.getLastName() + " . Thank You for Registering With us!");
            simpleMailMessage.setSubject("WELCOME");
            javaMailSender.send(simpleMailMessage);
        }
    }
}
