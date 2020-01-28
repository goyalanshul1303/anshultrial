package com.cartonwale.auth.api.dao;

import java.util.List;

import com.cartonwale.auth.api.model.Role;
import com.cartonwale.auth.api.model.User;
import com.cartonwale.common.dao.GenericDao;
import com.cartonwale.common.exception.DataAccessException;
import com.cartonwale.common.model.Permission;

public interface UserDao extends GenericDao<User>{
	
	User findByEmail(String email) throws DataAccessException;

	List<Permission> findUserPermissions()throws DataAccessException;
	
	User findUserByToken(String token)throws DataAccessException;

	User findByUsername(String username) throws DataAccessException;

	User findByUsernameOrEmail(String username, String email, Role role) throws DataAccessException;
	
}
