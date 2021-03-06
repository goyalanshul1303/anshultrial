package com.cartonwale.common.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cartonwale.common.model.EntityBase;

@Document(collection="Permission")
public class Permission extends EntityBase{

	public static String USER = "USER";
	public static String USER_ADMIN = "USER_ADMIN";
	public static String USER_SELLER_ADMIN = "USER_SELLER_ADMIN";
	public static String USER_PROVIDER = "USER_SELLER";
	public static String USER_CONSUMER = "USER_CONSUMER";
	
    private String code;

    public String getCode() {
        return this.code;
    }

    public void setCode(String name) {
        this.code = name;
    }

}
