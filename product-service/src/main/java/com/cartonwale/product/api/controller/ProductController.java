package com.cartonwale.product.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.common.util.ControllerBase;
import com.cartonwale.product.api.model.Product;
import com.cartonwale.product.api.service.ProductService;


@RestController
@RequestMapping("/product")
public class ProductController extends ControllerBase{
	
	private final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;
	
	@RequestMapping
    public ResponseEntity<List<Product>> getAll() {
		return makeResponse(productService.getAll());
    }
	
	@RequestMapping("/consumer/{consumerId}")
    public ResponseEntity<List<Product>> getAll(@PathVariable("consumerId") String consumerId) {
		return makeResponse(productService.getAll(consumerId));
    }
	
	@RequestMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") String id) {
		logger.info("Fetching Product Details for: " + id);
		logger.debug("Fetching Product Details for: " + id);
		logger.error("Fetching Product Details for: " + id);
		return makeResponse(productService.getById(id));
    }
	
	@RequestMapping("{consumerId}/{id}")
    public ResponseEntity<Product> getByConsumerIdAndId(@PathVariable("consumerId") String consumerId, @PathVariable("id") String id) {
		return makeResponse(productService.getById(consumerId, id));
    }
	
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Product> add(@RequestBody Product product) {
    	return makeResponse(productService.add(product), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Product> edit(@RequestBody Product product) {
    	return makeResponse(productService.edit(product), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping("/acceptingOffers")
	public ResponseEntity<List<Product>> productsAcceptingOffers() {
		return makeResponse(productService.getProductsAcceptingOffers());
	}
    
}
