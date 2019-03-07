package com.cartonwale.order.api.controller;

import java.util.List;

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
import com.cartonwale.order.api.model.Order;
import com.cartonwale.order.api.model.Quote;
import com.cartonwale.order.api.service.QuoteService;

@RestController
@RequestMapping("/quotes")
public class QuoteController extends ControllerBase {

	@Autowired
	private QuoteService quoteService;
	
	@Value("${jwt.header}")
    private String tokenHeader;
	
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Quote> addQuote(@RequestBody Quote quote) {
		return makeResponse(quoteService.add(quote));
	}
	
	@RequestMapping("/provider")
    public ResponseEntity<List<Quote>> getAllByProvider() {
		return makeResponse(quoteService.getAllByProvider());
    }
	
	@RequestMapping("/order/{id}")
    public ResponseEntity<List<Quote>> getAllByOrder(@PathVariable("id") String id) {
		return makeResponse(quoteService.getAllByOrder(id));
    }
	
	@RequestMapping("/{id}")
    public ResponseEntity<Quote> getById(@PathVariable("id") String id) {
		return makeResponse(quoteService.getById(id));
    }
	
	@RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Quote> edit(@RequestBody Quote quote) {
    	return makeResponse(quoteService.edit(quote), HttpStatus.ACCEPTED);
    }
	
	@RequestMapping(value = "/award", method = RequestMethod.POST)
    public ResponseEntity<Order> award(@ModelAttribute String quoteId) {
		return makeResponse(quoteService.awardOrder(quoteId), HttpStatus.OK);
	}
	
}
