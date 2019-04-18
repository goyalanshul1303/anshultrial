package com.cartonwale.product.api.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.product.api.dao.ProductDao;
import com.cartonwale.product.api.model.Product;
import com.cartonwale.product.api.model.ProductPrice;
import com.cartonwale.product.api.service.ProductPriceService;
import com.cartonwale.product.api.service.ProductService;

@Service
public class ProductServiceImpl extends GenericServiceImpl<Product> implements ProductService {
	
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
		return productDao.getAllByConsumer(consumerId);
	}

	@Override
	public List<Product> getProductsAcceptingOffers() {
		
		List<String> productIds = productPriceService.getProductsAcceptingOffers();
		return productDao.getByProductIds(productIds);
	}
}
