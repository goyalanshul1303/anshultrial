package com.cartonwale.auth.api.service;

import com.cartonwale.auth.api.model.User;
import com.cartonwale.common.service.GenericService;

import rx.Single;

public interface SellerService extends GenericService<User>{
	Single<User> registerOtherSeller(User seller);
}
