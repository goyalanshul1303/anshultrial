package com.cartonwale.order.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.common.util.ServiceUtil;
import com.cartonwale.order.api.dao.CartDao;
import com.cartonwale.order.api.exception.CartItemNotFoundException;
import com.cartonwale.order.api.model.Cart;
import com.cartonwale.order.api.model.CartItem;
import com.cartonwale.order.api.model.Product;
import com.cartonwale.order.api.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CartServiceImpl  extends GenericServiceImpl<Cart> implements CartService {

	private Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	
	@Autowired
	private CartDao cartDao;
	
	@Autowired
	private RestTemplate restTemplate;
	
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
			cart.getItems().remove(item);
			if(item.getQuantity() > 0){
				cart.getItems().add(item);
			}
			/*if(cart.getItems() != null && !cart.getItems().isEmpty() && cart.getItems().contains(item)){
				Optional<CartItem> cartItem = cart.getItems().stream().filter(ci -> ci.getProductId().equals(item.getProductId())).findAny();
				cartItem.get().setQuantity(cartItem.get().getQuantity() + item.getQuantity());
			} else {
				cart.getItems().add(item);
			}*/
			try {
				return cartDao.modify(cart);
			} catch (DataAccessException e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}

	@Override
	public Cart get(String authToken) {
		
		List<Cart> carts = new ArrayList<Cart>();
		carts = cartDao.getByUserId(SecurityUtil.getAuthUserDetails().getEntityId());
		
		Cart cart = new Cart(SecurityUtil.getAuthUserDetails().getEntityId());
		
		if(carts != null && !carts.isEmpty()){
			cart = carts.get(0);
		}
		
		addProductDetails(cart, authToken);
		
		return cart;
	}

	private void addProductDetails(Cart cart, String authToken) {
		List<String> productIds = cart.getItems().stream().map(i -> i.getProductId()).collect(Collectors.toList());
		
		ResponseEntity<List<Product>> responseEntity = ServiceUtil.callByType(HttpMethod.PUT, authToken,
				Arrays.asList(MediaType.APPLICATION_JSON), null, "http://PRODUCT-SERVICE/product/getByIds",
				getProductIdsAsString(productIds), restTemplate, new ParameterizedTypeReference<List<Product>>() {
				});
		
		List<Product> products = responseEntity.getBody();
		
		Map<String, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, p -> p));
		cart.getItems().stream().forEach(i -> i.setProduct(productMap.get(i.getProductId())));
	}
	
	private String getProductIdsAsString(List<String> productIds) {
		ObjectMapper mapper = new ObjectMapper();

		String json = null;
		try {
			json = mapper.writeValueAsString(productIds);
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
		logger.error(json);
		return json;
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
				int updatedQuatity = cartItem.get().getQuantity() - item.getQuantity();
				if(updatedQuatity == 0){
					cart.getItems().remove(item);
				} else {
					cartItem.get().setQuantity(cartItem.get().getQuantity() - item.getQuantity());
				}
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
