package com.cartonwale.auth.api.service.impl;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cartonwale.auth.api.dao.UserDao;
import com.cartonwale.auth.api.model.Role;
import com.cartonwale.auth.api.model.User;
import com.cartonwale.auth.api.security.SecurityUtil;
import com.cartonwale.auth.api.service.SellerService;
import com.cartonwale.auth.api.service.UserService;
import com.cartonwale.common.exception.BadRequestException;
import com.cartonwale.common.messages.ErrorMessage;
import com.cartonwale.common.service.impl.GenericServiceImpl;

import rx.Single;
import rx.exceptions.Exceptions;

@Service
public class SellerServiceImpl extends GenericServiceImpl<User> implements SellerService {
	
	@Autowired
	private UserDao sellerDao;
	
	@Autowired
	private UserService userService;
	
	@PostConstruct
	void init() {
		init(User.class, sellerDao);
	}

	@Override
	public Single<User> registerOtherSeller(User seller) {
		
		User loggedUser = SecurityUtil.getLoggedDbUser();
		
		return getById(loggedUser.getId()).flatMap(u->{
			
			String storeId = u.getSellerProfile().getStoreId();
			
			if(StringUtils.isEmpty(storeId))
				Exceptions.propagate(new BadRequestException(ErrorMessage.STORE_ID_IS_NULL));
			
			seller.getSellerProfile().setStoreId(storeId);
			seller.getRoles().add(Role.Create_Seller());
			seller.setBuyerProfile(null);
			
			return userService.add(seller);
			
		});
		
	}

}
