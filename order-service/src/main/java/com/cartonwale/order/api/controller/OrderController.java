package com.cartonwale.order.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.common.util.ControllerBase;
import com.cartonwale.order.api.model.Order;
import com.cartonwale.order.api.service.OrderService;


@RestController
@RequestMapping("/orders")
public class OrderController extends ControllerBase{

	@Autowired
	private OrderService orderService;
	
	@Value("${jwt.header}")
    private String tokenHeader;
	
	@RequestMapping
    public ResponseEntity<List<Order>> getAll() {
		return makeResponse(orderService.getAll());
    }
	
	@RequestMapping("/toUpdate")
    public ResponseEntity<List<Order>> getAllToUpdate() {
		return makeResponse(orderService.getAllToUpdate());
    }
	
	@RequestMapping("/consumer/requirements")
    public ResponseEntity<List<Order>> getRequirementsByConsumer() {
		return makeResponse(orderService.getRequirementsByConsumer());
    }
	
	@RequestMapping("/provider")
    public ResponseEntity<List<Order>> getAllByProvider() {
		return makeResponse(orderService.getAllByProvider());
    }
	
	@RequestMapping("/provider/completed")
    public ResponseEntity<List<Order>> getCompletedByProvider() {
		return makeResponse(orderService.getCompletedByProvider());
    }
	
	@RequestMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable("id") String id) {
		return makeResponse(orderService.getById(id));
    }
	
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Order> add(@RequestBody Order order, HttpServletRequest request) {
    	return makeResponse(orderService.add(order, request.getHeader(tokenHeader)), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Order> edit(@RequestBody Order order) {
    	return makeResponse(orderService.edit(order), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping("/placedOrders")
    public ResponseEntity<List<Order>> getPlacedOrder() {
		return makeResponse(orderService.getPlacedOrders());
    }
    
    @RequestMapping(value="/changeOrderStatus/{orderId}/{statusId}", method = RequestMethod.PUT)
    public ResponseEntity<Order> changeOrderStatus(@PathVariable("orderId") String orderId, @PathVariable("statusId") int statusId) {
		return makeResponse(orderService.changeStatus(orderId, statusId), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(value = "/abc/recentOrders", method = RequestMethod.PUT)
    public ResponseEntity<List<Order>> recentOrders(@RequestBody List<String> productIds) {
		return makeResponse(orderService.recentOrders(productIds));
    }
    
}
