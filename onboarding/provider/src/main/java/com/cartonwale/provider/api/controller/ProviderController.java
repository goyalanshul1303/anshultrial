package com.cartonwale.provider.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.common.async.response.AsyncResponseEntity;
import com.cartonwale.common.util.ControllerBase;
import com.cartonwale.provider.api.model.Provider;
import com.cartonwale.provider.api.service.ProviderService;


@RestController
@RequestMapping("/providers")
public class ProviderController extends ControllerBase{

	@Autowired
	private ProviderService productService;
	
	@RequestMapping
    public AsyncResponseEntity<Provider> getAll() {
		return makeAsyncResponse(productService.getAll());
    }
	
	@RequestMapping("/{id}")
    public AsyncResponseEntity<Provider> getById(@PathVariable("id") String id) {
		return makeAsyncResponse(productService.getById(id));
    }
	
	@RequestMapping(method = RequestMethod.POST)
    public AsyncResponseEntity<Provider> add(@ModelAttribute Provider product) {
    	return makeAsyncResponse(productService.add(product), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public AsyncResponseEntity<Provider> edit(@ModelAttribute Provider product) {
    	return makeAsyncResponse(productService.edit(product), HttpStatus.ACCEPTED);
    }
    
}
