package com.kverchi.diary.service.impl;

import com.kverchi.diary.model.Email;
import com.kverchi.diary.service.EmailService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Liudmyla Melnychuk on 8.2.2019.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceImplTest {

    @Autowired
    public EmailService emailService;

    @Ignore
    @Test
    public void testSendingEmail() {
        Email email = new Email("kverchi24@gmail.com", "Developing demo", "Developing stage: demo example.");
        emailService.sendEmail(email);
        assertTrue(true);
    }
}