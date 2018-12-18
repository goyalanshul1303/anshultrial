package com.cartonwale.provider.api.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartonwale.common.service.impl.GenericServiceImpl;
import com.cartonwale.provider.api.dao.ProviderDao;
import com.cartonwale.provider.api.model.Provider;
import com.cartonwale.provider.api.service.ProviderService;

@Service
public class ProviderServiceImpl extends GenericServiceImpl<Provider> implements ProviderService {
	
	@Autowired
	private ProviderDao productDao;
	
	@PostConstruct
	void init() {
		init(Provider.class, productDao);
	}
	
	@Override
	public Provider add(Provider provider) {
		
		return super.add(provider);
	}
	
	@Override
	public Provider edit(Provider provider) {
		
		return super.edit(provider);
	}
	
}
