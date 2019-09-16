package com.cartonwale.order.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Cart Item not found to be deducted")  //403
public class CartItemNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 4333940983178070455L;
	
	public CartItemNotFoundException() {
	}
	
	public CartItemNotFoundException(String message) {
		super(message);
	}

}
