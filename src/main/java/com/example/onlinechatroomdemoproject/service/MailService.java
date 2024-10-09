package com.example.onlinechatroomdemoproject.service;

import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Random;

import jakarta.mail.MessagingException;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    Random random = new Random();
    public String mailConfirmationCode(String to, String subject) throws MessagingException {

        String confirmCode = String.valueOf(random.nextInt(10000, 99999));
        MimeMessage mimeMessage= mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);

        helper.setSubject(subject);
        helper.setFrom(mailProperties.getUsername());
        helper.setText(confirmCode);
        helper.setTo(to);
        System.out.println(to);
        System.out.println(mailProperties.getUsername());
        mailSender.send(mimeMessage);
        System.out.println("Mail was sent ");

        return confirmCode;
    }
}
