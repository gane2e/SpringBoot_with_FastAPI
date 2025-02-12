package com.lms.service;

import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void emailServiceTest() {
        emailService.sendEmail("b1a409896@naver.com", "메일 테스트제목", "Mail-courseApplication");
    }



}