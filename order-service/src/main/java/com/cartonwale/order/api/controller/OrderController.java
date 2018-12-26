package com.cartonwale.order.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.common.security.AuthUser;
import com.cartonwale.common.util.ControllerBase;
import com.cartonwale.order.api.model.Order;
import com.cartonwale.order.api.service.OrderService;


@RestController
@RequestMapping("/orders")
public class OrderController extends ControllerBase{

	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/consumer")
    public ResponseEntity<List<Order>> getAll(Authentication authentication) {
		AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getDetails();
		System.out.println(user.getUserId());
		return makeResponse(orderService.getAllByConsumer(user.getUserId()));
    }
	
	@RequestMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable("id") String id) {
		return makeResponse(orderService.getById(id));
    }
	
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Order> add(@RequestBody Order order) {
    	return makeResponse(orderService.add(order), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Order> edit(@RequestBody Order order) {
    	return makeResponse(orderService.edit(order), HttpStatus.ACCEPTED);
    }
    
}
