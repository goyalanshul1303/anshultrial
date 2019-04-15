package com.cartonwale.product.api.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.product.api.dao.ProductPriceDao;
import com.cartonwale.product.api.model.Offer;
import com.cartonwale.product.api.model.ProductPrice;
import com.cartonwale.product.api.service.ProductPriceService;

public class ProductPriceServiceImpl extends GenericServiceImpl<ProductPrice> implements ProductPriceService{
	
	@Autowired
	ProductPriceDao priceDao;

	@PostConstruct
	void init() {
		init(ProductPrice.class, priceDao);
	}

	@Override
	public void startAcceptingOffers(List<String> productIds) {
		
		priceDao.updateOfferAcceptanceFlag(productIds, Boolean.TRUE);
	}

	@Override
	public void stopAcceptingOffers(List<String> productIds) {
		
		priceDao.updateOfferAcceptanceFlag(productIds, Boolean.FALSE);
	}

	@Override
	public void addPriceOffer(String productId, Offer offer) {
		
		priceDao.addOffer(productId, offer);
	}

	@Override
	public void setPrice(String productId, double price) {
		
		priceDao.setProductPrice(productId, price);
	}

	@Override
	public Double getPrice(String productId) {
		
		return priceDao.getProductPrice(productId);
	}

	@Override
	public List<String> getProductsAcceptingOffers() {
		
		return priceDao.getProductsAcceptingOffers();
	}
}