package com.cartonwale.provider.api.service;

import com.cartonwale.common.service.GenericService;
import com.cartonwale.provider.api.model.Provider;

public interface ProviderService extends GenericService<Provider>{

	void addProviderUser(Provider provider, String authToken);
}
