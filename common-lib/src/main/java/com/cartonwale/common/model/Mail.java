package com.cartonwale.common.model;

public class Mail {

	private String toAddress;
	
	private String subject;
	
	private String fromAddress;
	
	private String body;

	public Mail(String toAddress, String subject, String fromAddress, String body) {
		super();
		this.toAddress = toAddress;
		this.subject = subject;
		this.fromAddress = fromAddress;
		this.body = body;
	}

	public String getToAddress() {
		return toAddress;
	}

	public String getSubject() {
		return subject;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public String getBody() {
		return body;
	}
	
}
