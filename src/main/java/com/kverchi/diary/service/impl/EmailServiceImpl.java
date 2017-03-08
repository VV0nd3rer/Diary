package com.kverchi.diary.service.impl;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	public boolean sendEmailFromAdmin(String toAddr, String text) {
		/*boolean res = false;
		AdminEmailAddress adminEmail = null;
		adminEmail = emailDao.getById(ADMIN_EMAIL);
		if(adminEmail == null) {
			return res;
		}
		logger.info("Encoded pass is " + passwordEncoder.encode("n1c3_w0r1d"));
		if(mailSender instanceof JavaMailSenderImpl) {
			JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl)mailSender;
			javaMailSender.setUsername(adminEmail.getEmail());
			javaMailSender.setPassword(adminEmail.getPassword());
			
		
		
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
				@Override
				public void prepare(MimeMessage mimeMessage) throws Exception {
					mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
					mimeMessage.setFrom(new InternetAddress("kverchi24@gmail.com"));
					mimeMessage.setText(text);
				}
			};
			try {
				javaMailSender.send(preparator);
				res = true;
			} catch(MailException ex) {
				//logger.error(ex);
			}
		}
		return res;*/
		
		boolean res = false;
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
				mimeMessage.setFrom(new InternetAddress(ADMIN_EMAIL));
				mimeMessage.setText(text);
			}
		};
		try {
			mailSender.send(preparator);
			res = true;
		} catch(MailException ex) {
			logger.error(ex);
		}
		return res;
		
	}
}
