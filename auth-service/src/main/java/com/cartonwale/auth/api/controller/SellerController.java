package com.cartonwale.auth.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.cartonwale.auth.api.model.Role;
import com.cartonwale.auth.api.model.SellerProfile;
import com.cartonwale.auth.api.model.User;
import com.cartonwale.auth.api.service.UserService;
import com.cartonwale.common.async.response.AsyncResponseEntity;
import com.cartonwale.common.util.ControllerBase;

@RestController
@RequestMapping("/sellers")
public class SellerController extends ControllerBase {
	
	@Autowired
    private UserService userService;
    
    @RequestMapping(method = RequestMethod.POST)
    public AsyncResponseEntity<User> save(@ModelAttribute User user) {
    	user.getRoles().add(Role.Create_Seller_Admin());
    	
    	SellerProfile sellerProfile = new SellerProfile();
    	user.setSellerProfile(sellerProfile);
    	
    	user.setBuyerProfile(null);
        return makeAsyncResponse(userService.add(user), HttpStatus.CREATED);
    }


}
