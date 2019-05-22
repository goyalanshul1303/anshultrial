package com.cartonwale.product.api.dao.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.product.api.dao.ProductPriceDao;
import com.cartonwale.product.api.exception.ProductPriceException;
import com.cartonwale.product.api.model.Offer;
import com.cartonwale.product.api.model.ProductPrice;

@Repository
public class ProductPriceDaoImpl extends GenericDaoImpl<ProductPrice> implements ProductPriceDao {
	
	private final Logger logger = LoggerFactory.getLogger(ProductPriceDaoImpl.class);

	public ProductPriceDaoImpl() {
		super(ProductPrice.class);
	}

	@Override
	public void updateOfferAcceptanceFlag(List<String> productIds, boolean isAccepting) {

		Query query = new Query();
		query.addCriteria(Criteria.where("productId").in(productIds));

		Update update = new Update();
		update.set("isAcceptingOffers", isAccepting);

		try {
			super.findAndUpdateAll(query, update);
		} catch (DataAccessException e) {

			throw new ProductPriceException(e.getMessage());
		}
	}

	@Override
	public void addOffer(String productId, Offer offer) {

		Query query = new Query();
		query.addCriteria(Criteria.where("productId").is(productId));
		try {
			ProductPrice price = super.getAll(query).get(0);
			if(price.getOffers().stream().noneMatch(o -> o.getProviderId().equals(offer.getProviderId()))) {
				price.getOffers().add(offer);
				super.modify(price);
			} else {
				throw new ProductPriceException("Product Price already exists for this provider");
			}
		} catch (DataAccessException e) {

			throw new ProductPriceException(e.getMessage());
		}

	}

	@Override
	public void setProductPrice(String productId, double price) {

		Query query = new Query();
		query.addCriteria(Criteria.where("productId").is(productId));
		try {
			ProductPrice productPrice = super.getAll(query).get(0);
			productPrice.setPrice(price);
			super.modify(productPrice);
		} catch (DataAccessException e) {

			throw new ProductPriceException(e.getMessage());
		}
	}

	@Override
	public Double getProductPrice(String productId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("productId").is(productId));
		try {
			ProductPrice productPrice = super.getAll(query).get(0);
			return productPrice.getPrice();
		} catch (DataAccessException e) {

			throw new ProductPriceException(e.getMessage());
		}
	}

	@Override
	public List<String> getProductsAcceptingOffers() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isAcceptingOffers").is(true));
		try {
			return super.getAll(query).stream()
					.filter(p -> !p.getOffers().stream()
							.filter(o -> o.getProviderId().equals(SecurityUtil.getAuthUserDetails().getEntityId())).findFirst().isPresent())
					.map(p -> p.getProductId()).collect(Collectors.toList());
		} catch (DataAccessException e) {

			throw new ProductPriceException(e.getMessage());
		}
	}
	
	@Override
	public List<ProductPrice> getByProductIds(List<String> productId) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("productId").is(productId));
		logger.info("Finding ProductPrice by productId:" + productId);
		try {
			List<ProductPrice> productPrices = super.getAll(query);
			return productPrices;
		} catch (DataAccessException e) {

			throw new ProductPriceException(e.getMessage());
		}
	}
}
