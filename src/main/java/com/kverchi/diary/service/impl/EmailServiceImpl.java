package com.kverchi.diary.service.impl;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.kverchi.diary.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
	final static Logger logger = Logger.getLogger(EmailServiceImpl.class);
	final static String ADMIN_EMAIL = "kverchi24@gmail.com";
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public boolean sendEmailFromAdmin(final String toAddr, final String subject, final String text) {		
		boolean res = false;
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
				message.setSubject(subject);
				message.setTo(toAddr);
				message.setFrom(new InternetAddress(ADMIN_EMAIL));
				message.setText(text, true);
			}
		};
		try {
			mailSender.send(preparator);
			res = true;
		} catch(MailException ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
		return res;
	}
	
	@Override
	public boolean sendSimpleHTMLEmail(String toAddr, String subject, String text) {
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
       
        try {
        	 message.setSubject(subject);
             message.setFrom(ADMIN_EMAIL);
			 message.setTo(toAddr);
			 message.setText(text, true);
			 mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public boolean sendTextEmail(String toAddr, String subject, String text) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean sendHTMLWithAttachmentEmail(String toAddr, String subject, String text) {
		// TODO Auto-generated method stub
		return false;
	}
}
