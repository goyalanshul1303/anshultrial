package com.cartonwale.order.api.service;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.order.api.model.Cart;
import com.cartonwale.order.api.model.CartItem;

public interface CartService  extends GenericService<Cart> {

	Cart add(CartItem item);
	
	Cart get();
	
	Cart remove(CartItem item);

}
