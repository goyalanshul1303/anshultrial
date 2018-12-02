package com.cartonwale.provider.api.dao.impl;

import org.springframework.stereotype.Repository;
import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.provider.api.dao.ProviderDao;
import com.cartonwale.provider.api.model.Provider;

@Repository
public class ProviderDaoImpl extends GenericDaoImpl<Provider> implements ProviderDao{
	
	public ProviderDaoImpl() {
		super(Provider.class);
	}
}
