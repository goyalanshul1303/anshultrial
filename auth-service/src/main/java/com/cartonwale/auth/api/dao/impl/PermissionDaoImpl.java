package com.cartonwale.auth.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.cartonwale.auth.api.dao.PermissionDao;
import com.cartonwale.common.dao.impl.GenericDaoImpl;
import com.cartonwale.common.model.Permission;

@Repository
public class PermissionDaoImpl extends GenericDaoImpl<Permission> implements PermissionDao {

	public PermissionDaoImpl() {
		super(Permission.class);
	}
}
