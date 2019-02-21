package com.cartonwale.order.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartonwale.common.util.ControllerBase;
import com.cartonwale.order.api.model.Order;
import com.cartonwale.order.api.service.OrderService;

@RestController
@RequestMapping("/provider/orders")
public class ProviderOrderController extends ControllerBase {

	@Autowired
	private OrderService orderService;
	
	@RequestMapping
    public ResponseEntity<List<Order>> getPlacedOrder() {
		return makeResponse(orderService.getAll());
    }
}
