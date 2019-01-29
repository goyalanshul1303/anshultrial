package com.cartonwale.product.api.controller;

import java.util.List;

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

	@Autowired
	private ProductService productService;
	
	@RequestMapping
    public ResponseEntity<List<Product>> getAll() {
		return makeResponse(productService.getAll());
    }
	
	@RequestMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") String id) {
		return makeResponse(productService.getById(id));
    }
	
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Product> add(@RequestBody Product product) {
    	return makeResponse(productService.add(product), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Product> edit(@RequestBody Product product) {
    	return makeResponse(productService.edit(product), HttpStatus.ACCEPTED);
    }
    
}
