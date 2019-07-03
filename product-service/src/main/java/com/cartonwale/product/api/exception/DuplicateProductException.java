package com.cartonwale.product.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Product name already exists")  //403
public class DuplicateProductException extends RuntimeException{

	private static final long serialVersionUID = 4333940983178070455L;
	
	public DuplicateProductException() {
	}
	
	public DuplicateProductException(String message) {
		super(message);
	}

}
