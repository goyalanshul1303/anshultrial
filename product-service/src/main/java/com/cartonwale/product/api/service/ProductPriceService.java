package com.cartonwale.product.api.service;

import java.util.List;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.product.api.model.Offer;
import com.cartonwale.product.api.model.ProductPrice;

public interface ProductPriceService extends GenericService<ProductPrice>{

	public void startAcceptingOffers(List<String> productIds);

	public void stopAcceptingOffers(List<String> productIds);

	public void addPriceOffer(String productId, Offer offer);

	public void setPrice(String productId, double price);

	public Double getPrice(String productId);
	
	public List<String> getProductsAcceptingOffers();

	public ProductPrice getByProductId(String productId);

	List<ProductPrice> getByProductIds(List<String> productIds);

}
