package com.kverchi.diary.domain;

import org.springframework.http.HttpStatus;

public class ServiceResponse<T> {
	
	private HttpStatus respCode;
	private String respMsg;
	private T responseObject;
	
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
	public T getResponseObject() {
		return responseObject;
	}
	public void setResponseObject(T responseObject) {
		this.responseObject = responseObject;
	}
}
