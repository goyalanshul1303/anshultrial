package com.cartonwale.auth.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.cartonwale.auth.api.dao.RoleDao;
import com.cartonwale.auth.api.model.Role;
import com.cartonwale.common.dao.impl.GenericDaoImpl;

@Repository
public class RoleDaoImpl extends GenericDaoImpl<Role> implements RoleDao {

	public RoleDaoImpl() {
		super(Role.class);
	}
	
}
