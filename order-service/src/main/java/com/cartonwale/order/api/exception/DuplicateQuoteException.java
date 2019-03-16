package com.cartonwale.order.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Quote already exists for this order")  //403
public class DuplicateQuoteException extends RuntimeException{

	private static final long serialVersionUID = 4333940983178070455L;
	
	public DuplicateQuoteException() {
	}
	
	public DuplicateQuoteException(String message) {
		super(message);
	}

}
