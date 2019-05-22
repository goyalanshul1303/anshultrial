package com.cartonwale.product.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.common.util.ServiceUtil;
import com.cartonwale.product.api.dao.ProductDao;
import com.cartonwale.product.api.model.Order;
import com.cartonwale.product.api.model.Product;
import com.cartonwale.product.api.model.ProductPrice;
import com.cartonwale.product.api.service.ProductPriceService;
import com.cartonwale.product.api.service.ProductService;

@Service
public class ProductServiceImpl extends GenericServiceImpl<Product> implements ProductService {
	
	private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductPriceService productPriceService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PostConstruct
	void init() {
		init(Product.class, productDao);
	}
	
	@Override
	public Product add(Product product) {
		Product productNew = super.add(product);
		
		productPriceService.add(new ProductPrice(productNew.getId()));
		return productNew;
	};
	
	@Override
	public Product edit(Product product) {
		
		return super.edit(product);
	}
	
	@Override
	public List<Product> getAll(){
		
		return productDao.getAllByConsumer(SecurityUtil.getAuthUserDetails().getEntityId());
	}

	
	@Override
	public Product getById(String consumerId, String id) {

		return productDao.getById(consumerId, id);
	}

	@Override
	public List<Product> getAll(String consumerId, String authToken) {
		List<Product> products = productDao.getAllByConsumer(consumerId);
		products.stream().map(product -> {
			product.setPrice(100.0);
			Order lastOrder = new Order();			
			return product;			
		});
		
		products.stream().peek(p -> logger.debug("Product Price: " + p.getPrice()));
		List<String> productIds = products.stream().map(product -> product.getId()).collect(Collectors.toList());
		
		ResponseEntity<List<Order>> responseEntity = (ResponseEntity<List<Order>>) ServiceUtil.callByType(HttpMethod.GET,
				authToken, null, null, "http://ORDER-SERVICE/orders/abc/recentOrders",
				productIds.stream().collect(Collectors.joining(",")), restTemplate, new ParameterizedTypeReference<List<Order>>() {});
		
		List<Order> orders = responseEntity.getBody();
		
		return products ;
	}

	@Override
	public List<Product> getProductsAcceptingOffers() {
		
		List<String> productIds = productPriceService.getProductsAcceptingOffers();
		return productDao.getByProductIds(productIds);
	}
}
