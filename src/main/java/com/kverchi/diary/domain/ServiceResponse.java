package com.kverchi.diary.domain;

import org.springframework.http.HttpStatus;

public class ServiceResponse {
	
	private HttpStatus respCode;
	private String respMsg;
	public ServiceResponse() {}
	public ServiceResponse(HttpStatus code, String msg) {
		this.respCode = code;
		this.respMsg = msg;
	}
	public HttpStatus getRespCode() {
		return respCode;
	}
	public void setRespCode(HttpStatus respCode) {
		this.respCode = respCode;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
}
