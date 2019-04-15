package com.cartonwale.product.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Some Exception Occurred")  //403
public class ProductPriceException extends RuntimeException{

	private static final long serialVersionUID = 4333940983178070455L;
	
	public ProductPriceException() {
	}
	
	public ProductPriceException(String message) {
		super(message);
	}

}
