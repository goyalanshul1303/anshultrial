package com.cartonwale.product.api.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.common.util.ControllerBase;
import com.cartonwale.product.api.model.AlContainer;
import com.cartonwale.product.api.model.ConsumerProduct;
import com.cartonwale.product.api.model.CorrugatedBox;
import com.cartonwale.product.api.model.Product;
import com.cartonwale.product.api.model.ProductImageDto;
import com.cartonwale.product.api.model.ProductSpecification;
import com.cartonwale.product.api.model.ProductType;
import com.cartonwale.product.api.model.ProviderProduct;
import com.cartonwale.product.api.model.Tape;
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
	
	@RequestMapping("/provider/{providerId}")
    public ResponseEntity<List<Product>> getAllByProvider(@PathVariable("providerId") String providerId, HttpServletRequest request) {
		return makeResponse(productService.getAllByProvider(providerId, request.getHeader(tokenHeader)));
    }
	
	@RequestMapping("/getByIds")
	public ResponseEntity<List<Product>> getByIds(@RequestBody List<String> productIds) {
		
		return makeResponse(productService.getAllByIds(productIds), HttpStatus.FOUND);
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
    public ResponseEntity<Product> add(@RequestBody Product product, HttpServletRequest request) {
    	return makeResponse(productService.add(product, request.getHeader(tokenHeader)), HttpStatus.CREATED);
    }
	
	@RequestMapping(value="/raw", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Product> add(HttpEntity<String> product, HttpServletRequest request) {
		
		Product productObj = null;
		try {
			productObj = parseJSON(product.getBody());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
    	return makeResponse(productService.add(productObj, request.getHeader(tokenHeader)), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Product> edit(@RequestBody Product product) {
    	return makeResponse(productService.edit(product), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping("/acceptingOffers")
	public ResponseEntity<List<Product>> productsAcceptingOffers() {
		return makeResponse(productService.getProductsAcceptingOffers());
	}
    
    @RequestMapping(value = "/uploadProductImage/{id}", method = RequestMethod.POST)
	public ResponseEntity<Boolean> uploadProductImage(@ModelAttribute ProductImageDto productImageDto, @PathVariable("id") String id) {
		return makeResponse(productService.uploadProductImage(productImageDto, id));
	}
    
    @RequestMapping(value = "/productImage/{id}", method = RequestMethod.GET)
   	public ResponseEntity<byte[]> getProductImage(@PathVariable("id") String id) {
    	
    	byte[] bytes = productService.getProductImage(id);
   		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "image/*");
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
   	}
    
    public Product parseJSON(String json) throws JsonProcessingException, IOException {
		
		ObjectMapper objectMapper = new ObjectMapper();
    	JsonNode root = objectMapper.readTree(json);
    	
    	int type = root.get("productType").asInt();
    	
    	
    	Gson gson = new Gson();
    	Product product = null;
    	ProductSpecification specs = null;
    	
    	if(root.get("consumerId") != null) {
    		product = gson.fromJson(json, ConsumerProduct.class);
    		product.setUserId(root.get("consumerId").textValue());
    	}
    	else if(root.get("providerId") != null) {
    		product = gson.fromJson(json, ProviderProduct.class);
    		product.setUserId(root.get("providerId").textValue());
    	}
    	
    	switch(ProductType.getProductType(type)) {
    		case CORRUGATED_CARTON:    	
    			specs = objectMapper.convertValue(root.get("specifications"), CorrugatedBox.class);
    			break;
    		case TAPE:
    			specs = objectMapper.convertValue(root.get("specifications"), Tape.class);
    			break;
    		case ALUMINIUM_CONTAINER:
    			specs = objectMapper.convertValue(root.get("specifications"), AlContainer.class);
    			break;
    		default:
    			break;
    	}
    	
    	product.setSpecifications(specs);
    	
		return product;
	}
    
    @Test
    public void testParseJSON(){
    	String json = "{\"name\": \"Product Raw AL\",\"providerId\": \"5c5b2db59943f200010bf6bf\",\"productType\": 3,\"quantity\": 123,\"category\": 1,\"specifications\": {" +
    			    "\"vol\": 50,\"thickness\": 60}}";
    	Product product = null;
    	try {
			product = parseJSON(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	System.out.println(product);
    }
    
}
