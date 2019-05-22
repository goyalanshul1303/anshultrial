package com.cartonwale.product.api.dao;

import java.util.List;

import com.cartonwale.common.dao.GenericDao;
import com.cartonwale.product.api.model.Offer;
import com.cartonwale.product.api.model.ProductPrice;

public interface ProductPriceDao  extends GenericDao<ProductPrice> {

	void updateOfferAcceptanceFlag(List<String> productIds, boolean isAccepting);

	void addOffer(String productId, Offer offer);

	void setProductPrice(String productId, double price);

	Double getProductPrice(String productId);

	List<String> getProductsAcceptingOffers();

	List<ProductPrice> getByProductIds(List<String> productId);

}
