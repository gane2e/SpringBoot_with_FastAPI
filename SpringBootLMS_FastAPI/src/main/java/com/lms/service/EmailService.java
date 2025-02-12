package com.lms.service;

import groovy.util.logging.Slf4j;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;


    //인증번호를 포함하여 발송하는 메서드
    @Async
    public void sendEmailIdFind(String email, String subject, String templateName, int certificationNumber) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(email);  // 메일 수신자
            helper.setSubject(subject); // 메일 제목
            helper.setText(setContextIdFind(templateName, certificationNumber), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.info("Failed to send email to " + email, e);
            throw new RuntimeException(e);
        }
    }
    
    //기본 안내만 발송하는 메서드
    @Async
    public void sendEmail(String email, String subject, String templateName) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            System.out.println("email => " + email);
            System.out.println("subject => " + subject);
            System.out.println("templateName => " + templateName);
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(email);  // 메일 수신자
            helper.setSubject(subject); // 메일 제목
            helper.setText(setContext(todayDate(),templateName), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.info("Failed to send email to " + email, e);
            throw new RuntimeException(e);
        }
    }

    public String todayDate(){
        ZonedDateTime todayDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).atZone(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일");
        return todayDate.format(formatter);
    }


    //일반 안내용 컨텍스트
    public String setContext(String date, String templateName) {
        Context context = new Context();
        context.setVariable("date", date);
        return templateEngine.process(templateName, context);
    }
    
    //인증번호 발송용 컨텍스트
    public String setContextIdFind(String templateName, int certificationNumber) {
        Context context = new Context();
        context.setVariable("certificationNumber", certificationNumber);
        return templateEngine.process(templateName, context);
    }


}
