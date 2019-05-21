package com.cartonwale.product.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.common.util.ControllerBase;

@RestController
@RequestMapping("/batch")
public class BatchController  extends ControllerBase {

	@RequestMapping("/stopAcceptingPrice")
	public ResponseEntity<String> stopAcceptingPrice() {
		return makeResponse("success");
	}
}
