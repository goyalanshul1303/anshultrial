package com.cartonwale.provider.api.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartonwale.common.security.SecurityUtil;
import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.provider.api.dao.ProviderDao;
import com.cartonwale.provider.api.model.Provider;
import com.cartonwale.provider.api.service.ProviderService;

import rx.Single;

@Service
public class ProviderServiceImpl extends GenericServiceImpl<Provider> implements ProviderService {
	
	@Autowired
	private ProviderDao productDao;
	
	@PostConstruct
	void init() {
		init(Provider.class, productDao);
	}
	
	@Override
	public Single<Provider> add(Provider product) {
		String sellerId = SecurityUtil.getAuthUserDetails().getUserId();
		product.setSellerId(sellerId);
		return super.add(product);
	}
	
	@Override
	public Single<Provider> edit(Provider product) {
		String sellerId = SecurityUtil.getAuthUserDetails().getUserId();
		product.setSellerId(sellerId);
		return super.edit(product);
	}
	
}
