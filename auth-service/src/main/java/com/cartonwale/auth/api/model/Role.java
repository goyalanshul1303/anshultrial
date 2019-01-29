package com.cartonwale.auth.api.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cartonwale.common.model.EntityBase;
import com.cartonwale.common.model.Permission;

@Document(collection="Role")
public class Role extends EntityBase{
	
    public static final String ROLE_ADMIN = "role.admin";
    public static final String ROLE_SELLER_ADMIN = "role.seller.admin";
    public static final String ROLE_PROVIDER = "role.provider";
    public static final String ROLE_CONSUMER = "role.consumer";

    private String code;

    @DBRef
    private List<Permission> permissions = new ArrayList<>();

    public Role() {
	}
    
    public Role(String id){
    	setId(id);
    }
    
    public String getCode() {
        return this.code;
    }

    public void setCode(String name) {
        this.code = name;
    }

    @JsonIgnore
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	
	public static Role Create_Admin(){
		return new Role(Role.ROLE_ADMIN);
	}
	
	public static Role Create_Seller_Admin(){
		return new Role(Role.ROLE_SELLER_ADMIN);
	}
	
	public static Role Create_Provider(){
		return new Role(Role.ROLE_PROVIDER);
	}
	
	public static Role Create_Consumer(){
		return new Role(Role.ROLE_CONSUMER);
	}
	
	public static boolean checkUserIs_Admin(User user){
		return checkUserType(user, Role.Create_Admin());
	}
	
	public static boolean checkUserIs_Seller_Admin(User user){
		return checkUserType(user, Role.Create_Seller_Admin());
	}
	
	public static boolean checkUserIs_Seller(User user){
		return checkUserType(user, Role.Create_Provider());
	}
	
	public static boolean checkUserIs_Buyer(User user){
		return checkUserType(user, Role.Create_Consumer());
	}
	
	public static boolean checkUserType(User user, Role... roles){
		
		for(Role checkRole : roles){
			
			if(user.getRoles().contains(checkRole))
				return true;
				
		}

		return false;
		
		
	}
	
	
	
}
