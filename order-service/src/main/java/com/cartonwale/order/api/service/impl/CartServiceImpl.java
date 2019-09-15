package com.cartonwale.order.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.order.api.dao.CartDao;
import com.cartonwale.order.api.exception.CartItemNotFoundException;
import com.cartonwale.order.api.model.Cart;
import com.cartonwale.order.api.model.CartItem;
import com.cartonwale.order.api.service.CartService;

@Service
public class CartServiceImpl  extends GenericServiceImpl<Cart> implements CartService {

	private Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	
	@Autowired
	private CartDao cartDao;
	
	@PostConstruct
	void init() {
		init(Cart.class, cartDao);
	}
	
	@Override
	public Cart add(CartItem item) {
		
		List<Cart> carts = new ArrayList<Cart>();
		carts = cartDao.getByUserId(SecurityUtil.getAuthUserDetails().getEntityId());
		
		if(carts == null || carts.isEmpty()){
			Cart cart = new Cart(SecurityUtil.getAuthUserDetails().getEntityId());
			cart.getItems().add(item);
			
			try{
				return cartDao.add(cart);
			} catch(DataAccessException e) {
				logger.error(e.getMessage());
			}
		} else {
			Cart cart = carts.get(0);
			if(cart.getItems() != null && !cart.getItems().isEmpty() && cart.getItems().contains(item)){
				Optional<CartItem> cartItem = cart.getItems().stream().filter(ci -> ci.getProductId().equals(item.getProductId())).findAny();
				cartItem.get().setQuantity(cartItem.get().getQuantity() + item.getQuantity());
			} else {
				cart.getItems().add(item);
			}
			try {
				return cartDao.modify(cart);
			} catch (DataAccessException e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}

	@Override
	public Cart get() {
		
		List<Cart> carts = new ArrayList<Cart>();
		carts = cartDao.getByUserId(SecurityUtil.getAuthUserDetails().getEntityId());
		
		if(carts != null && !carts.isEmpty()){
			return carts.get(0);
		} else {
			return new Cart(SecurityUtil.getAuthUserDetails().getEntityId());
		}
	}

	@Override
	public Cart remove(CartItem item) {
		List<Cart> carts = new ArrayList<Cart>();
		carts = cartDao.getByUserId(SecurityUtil.getAuthUserDetails().getEntityId());
		
		if(carts.isEmpty()){
			throw new CartItemNotFoundException("Cart is Empty");
		} else {
			Cart cart = carts.get(0);
			if(cart.getItems() != null && !cart.getItems().isEmpty() && cart.getItems().contains(item)){
				Optional<CartItem> cartItem = cart.getItems().stream().filter(ci -> ci.getProductId().equals(item.getProductId())).findAny();
				cartItem.get().setQuantity(cartItem.get().getQuantity() - item.getQuantity());
			} else {
				throw new CartItemNotFoundException("Cart Item not found");
			}
			try {
				return cartDao.modify(cart);
			} catch (DataAccessException e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}

}
