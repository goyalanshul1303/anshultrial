package com.cartonwale.common.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.cartonwale.common.model.JwtUser;
import com.cartonwale.common.model.User;

public class SecurityUtil {

	public static UserDetails getLoggedUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
        	return (UserDetails) principal;

        return null;
    }
	
	public static AuthUser getAuthUserDetails() {
        UserDetails userDetails = getLoggedUserDetails();

        if (userDetails != null && (userDetails instanceof AuthUser))
        	return (AuthUser) userDetails;

        return null;
    }
	
	public static JwtUser getLoggedJwtUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
        	
        	UserDetails userDetails = (UserDetails) principal;
        	JwtUser jwtUser = (JwtUser) userDetails;
        	
            return jwtUser;
        }

        return null;
    }
	
	public static User getLoggedDbUser() {
        return getLoggedJwtUser().getDbUser();
    }
	
}
