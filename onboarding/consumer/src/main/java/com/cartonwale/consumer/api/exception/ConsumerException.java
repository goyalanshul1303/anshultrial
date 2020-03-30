package com.cartonwale.consumer.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Some DB Exception Occurred")  //500
public class ConsumerException extends RuntimeException{

	private static final long serialVersionUID = 4333940983178070455L;
	
	public ConsumerException() {
	}
	
	public ConsumerException(String message) {
		super(message);
	}

}
