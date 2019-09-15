package com.cartonwale.order.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.common.util.ControllerBase;
import com.cartonwale.order.api.model.Cart;
import com.cartonwale.order.api.model.CartItem;
import com.cartonwale.order.api.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController extends ControllerBase {
	
	@Autowired
	private CartService cartService;

	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Cart> add(@RequestBody CartItem item) {
    	return makeResponse(cartService.add(item), HttpStatus.ACCEPTED);
    }
	
	@RequestMapping
	public ResponseEntity<Cart> get() {
		return makeResponse(cartService.get(), HttpStatus.FOUND);
	}
	
	@RequestMapping
	public ResponseEntity<Cart> remove(@RequestBody CartItem item) {
    	return makeResponse(cartService.remove(item), HttpStatus.ACCEPTED);
    }
}
