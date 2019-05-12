package com.cartonwale.product.api.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.product.api.dao.ProductDao;
import com.cartonwale.product.api.model.Order;
import com.cartonwale.product.api.model.OrderStatus;
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
	
	@PostConstruct
	void init() {
		init(Product.class, productDao);
	}
	
	/*@Override
	public Product add(Product product) {
		
		product.setConsumerId(SecurityUtil.getAuthUserDetails().getEntityId());
		return super.add(product);
	}*/
	
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

	/*@Override
	public Product getById(String id) {
		
		return productDao.getById(id);
	}*/
	
	@Override
	public Product getById(String consumerId, String id) {

		return productDao.getById(consumerId, id);
	}

	@Override
	public List<Product> getAll(String consumerId) {
		List<Product> products = productDao.getAllByConsumer(consumerId);
		products.stream().map(product -> {
			product.setPrice(100.0);
			Order lastOrder = new Order();
			if(product.getId().equals("5cd6bfebad0cf20001abcfef")){
				lastOrder.setOrderStatus(OrderStatus.ORDER_COMPLETED);
				lastOrder.setQuantity(200);
				Calendar cal = Calendar.getInstance();
				cal.set(2019, 3, 21);
				lastOrder.setOrderDate(cal.getTime());
			}
			else if (product.getId().equals("5cd6bf28ad0cf20001abcfed")){
				lastOrder.setOrderStatus(OrderStatus.MANUFACTURING_INITIATED);
				lastOrder.setQuantity(300);
				Calendar cal = Calendar.getInstance();
				cal.set(2019, 4, 28);
				lastOrder.setOrderDate(cal.getTime());
			}
			product.setLastOrder(lastOrder);
			return product;			
		});
		
		products.stream().peek(p -> logger.debug("Product Price: " + p.getPrice()));
		List<String> productIds = products.stream().map(product -> product.getId()).collect(Collectors.toList());
		
		return products ;
	}

	@Override
	public List<Product> getProductsAcceptingOffers() {
		
		List<String> productIds = productPriceService.getProductsAcceptingOffers();
		return productDao.getByProductIds(productIds);
	}
}
