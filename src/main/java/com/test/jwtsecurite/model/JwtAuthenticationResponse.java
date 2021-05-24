package com.test.jwtsecurite.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by ladji103.
 */
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
//    private Role role;
    private Collection<? extends GrantedAuthority> roles;

    public JwtAuthenticationResponse(String accessToken, Collection<? extends GrantedAuthority> roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(Collection<? extends GrantedAuthority> roles) {
        this.roles = roles;
    }
}
