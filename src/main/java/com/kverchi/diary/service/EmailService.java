package com.kverchi.diary.service;

public interface EmailService {
	boolean sendEmailFromAdmin(String toAddr, String text);
}
