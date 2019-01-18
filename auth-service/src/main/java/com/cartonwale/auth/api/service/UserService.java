package com.cartonwale.auth.api.service;

import java.util.List;

import com.cartonwale.auth.api.dto.UserImageDto;
import com.cartonwale.auth.api.model.User;
import com.cartonwale.common.exception.ServiceException;
import com.cartonwale.common.service.GenericService;

import rx.Single;

public interface UserService extends GenericService<User>{
	
	/*Single<*/User/*>*/ findByEmail(String email);
	
	/*Single<*/User/*>*/ findByUsernameOrEmail(String username, String email);
    
	User edit(User user, UserImageDto imageDto) throws ServiceException;
	
	Single<Boolean> delete(String id);

	Single<Boolean> delete(List<String> idList);
    
	Single<User> findUserByToken(String token);

	Boolean changePassword(String oldPassword, String newPassword);

	User findByUsername(String username);
	
}
