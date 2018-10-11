package com.qurasense.userApi.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private final String userId;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String aUserId) {
        super(username, password, authorities);
        userId = aUserId;
    }

    public String getUserId() {
        return userId;
    }
}
