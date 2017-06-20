package com.kverchi.diary.service;

public interface EmailService {
	boolean sendEmailFromAdmin(String toAddr, String subject, String text);
	boolean sendSimpleHTMLEmail(String toAddr, String subject, String text);
	boolean sendTextEmail(String toAddr, String subject, String text);
	boolean sendHTMLWithAttachmentEmail(String toAddr, String subject, String text);
}
