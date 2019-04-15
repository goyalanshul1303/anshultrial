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
import com.cartonwale.product.api.model.Offer;
import com.cartonwale.product.api.model.ProductPrice;
import com.cartonwale.product.api.service.ProductPriceService;

@RestController
@RequestMapping("/price")
public class ProductPriceController extends ControllerBase {

	@Autowired
	private ProductPriceService priceService;

	@RequestMapping("/{productId}")
	public ResponseEntity<ProductPrice> getById(@PathVariable("productId") String productId) {
		return makeResponse(priceService.getById(productId));
	}

	@RequestMapping(method = RequestMethod.POST, path = "/startAcceptingOffers")
	public ResponseEntity<Boolean> startAcceptingOffers(@RequestBody List<String> productIds) {
		priceService.startAcceptingOffers(productIds);
		return makeResponse(Boolean.TRUE, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/stopAcceptingOffers")
	public ResponseEntity<Boolean> stopAcceptingOffers(@RequestBody List<String> productIds) {
		priceService.stopAcceptingOffers(productIds);
		return makeResponse(Boolean.TRUE, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/addOffer/{productId}")
	public ResponseEntity<Boolean> addOffer(@PathVariable("productId") String productId, @RequestBody Offer offer) {
		priceService.addPriceOffer(productId, offer);
		return makeResponse(Boolean.TRUE, HttpStatus.ACCEPTED);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/{productId}/{price}")
	public ResponseEntity<Boolean> setPrice(@PathVariable("productId") String productId,
			@PathVariable("price") String price) {

		priceService.setPrice(productId, Double.parseDouble(price));
		return makeResponse(Boolean.TRUE, HttpStatus.ACCEPTED);
	}

	@RequestMapping("/consumer/{productId}")
	public ResponseEntity<Double> getPrice(@PathVariable("productId") String productId) {
		return makeResponse(priceService.getPrice(productId));
	}
	
}
