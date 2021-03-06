package com.cartonwale.consumer.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.common.util.ControllerBase;
import com.cartonwale.consumer.api.model.Consumer;
import com.cartonwale.consumer.api.model.Product;
import com.cartonwale.consumer.api.service.ConsumerService;


@RestController
@RequestMapping("/consumers")
public class ConsumerController extends ControllerBase{

	@Autowired
	private ConsumerService consumerService;
	
	@Value("${jwt.header}")
    private String tokenHeader;
	
	@RequestMapping
    public ResponseEntity<List<Consumer>> getAll() {
		return makeResponse(consumerService.getAll());
    }
	
	@RequestMapping("/{id}")
    public ResponseEntity<Consumer> getById(@PathVariable("id") String id) {
		return makeResponse(consumerService.getById(id));
    }
	
	@RequestMapping("boundedConsumers/{providerId}")
    public ResponseEntity<List<Consumer>> getBoundedConsumersByProviderId(@PathVariable("providerId") String providerId, HttpServletRequest request) {
		return makeResponse(consumerService.getBoundedConsumersByProviderId(providerId));
    }
	
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Consumer> add(@RequestBody Consumer consumer, HttpServletRequest request) {
		Consumer createdConsumer = consumerService.add(consumer);
		consumerService.addConsumerUser(createdConsumer, request.getHeader(tokenHeader));
    	return makeResponse(createdConsumer, HttpStatus.CREATED);
    }
	
	@RequestMapping(value = "/consumerOnly", method = RequestMethod.POST)
    public ResponseEntity<Consumer> addConsumerOnly(@RequestBody Consumer consumer) {
		Consumer createdConsumer = consumerService.add(consumer);
    	return makeResponse(consumer, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Consumer> edit(@ModelAttribute Consumer consumer) {
    	return makeResponse(consumerService.edit(consumer), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping("/products")
    public ResponseEntity<List<Product>> getProducts(HttpServletRequest request) {
		return makeResponse(consumerService.getProducts(request.getHeader(tokenHeader)));
    }
    
}
