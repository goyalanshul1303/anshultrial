package com.cartonwale.product.api.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.common.util.ControllerBase;
import com.cartonwale.product.api.model.AlContainer;
import com.cartonwale.product.api.model.Product;
import com.cartonwale.product.api.model.ProductType;
import com.cartonwale.product.api.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


@RestController
@RequestMapping("/product")
public class ProductController extends ControllerBase{
	
	private final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Value("${jwt.header}")
    private String tokenHeader;

	@Autowired
	private ProductService productService;
	
	@RequestMapping
    public ResponseEntity<List<Product>> getAll(HttpServletRequest request) {
		return makeResponse(productService.getAll(request.getHeader(tokenHeader)));
    }
	
	@RequestMapping("/consumer/{consumerId}")
    public ResponseEntity<List<Product>> getAll(@PathVariable("consumerId") String consumerId) {
		return makeResponse(productService.getAllByConsumer(consumerId));
    }
	
	@RequestMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") String id) {
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
	
	@RequestMapping(value="/raw", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Product> add(HttpEntity<String> product) {
		
		Product productObj = null;
		try {
			productObj = parseJSON(product.getBody());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
    	return makeResponse(productService.add(productObj), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Product> edit(@RequestBody Product product) {
    	return makeResponse(productService.edit(product), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping("/acceptingOffers")
	public ResponseEntity<List<Product>> productsAcceptingOffers() {
		return makeResponse(productService.getProductsAcceptingOffers());
	}
    
    public Product parseJSON(String json) throws JsonProcessingException, IOException {
		
		ObjectMapper objectMapper = new ObjectMapper();
    	JsonNode root = objectMapper.readTree(json);
    	
    	int type = root.get("productType").asInt();
    	
    	Gson gson = new Gson();
    	switch(ProductType.getProductType(type)) {
    		case CORRUGATED_CARTON:
    			gson.fromJson(json, Product.class);
    			break;
    		case TAPE:
    			break;
    		case ALUMINIUM_CONTAINER:
    			gson.fromJson(json, AlContainer.class);
    			break;
    		default:
    			break;
    	}
    	
		
		
		return new Product();
	}
    
}
