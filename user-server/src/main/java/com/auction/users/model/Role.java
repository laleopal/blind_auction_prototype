package com.auction.users.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    BUYER, SELLER;

    @Override
    public String getAuthority() {
        return name();
    }
}
