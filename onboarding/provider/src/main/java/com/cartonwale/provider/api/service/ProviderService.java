package com.cartonwale.provider.api.service;

import java.util.List;

import com.cartonwale.common.exception.ServiceException;
import com.cartonwale.common.service.GenericService;
import com.cartonwale.provider.api.model.Product;
import com.cartonwale.provider.api.model.Provider;

public interface ProviderService extends GenericService<Provider>{

	void addProviderUser(Provider provider, String authToken);

	List<Product> getProducts(String token);
}
