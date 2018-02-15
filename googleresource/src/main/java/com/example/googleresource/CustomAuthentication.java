package com.example.googleresource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mult-e on 23/07/2017.
 */
public class CustomAuthentication implements Authentication {

    private Collection<? extends GrantedAuthority> grantedAuthorities;
    private Object credentials;
    private Object details;
    private Object principal;
    private String name;
    private boolean authenticated;
    private String username;
    private Collection<SimpleGrantedAuthority> authorities ;


    public CustomAuthentication(final String username, final OAuth2Authentication oAuth2Authentication, final Collection<String> roles){
        this.credentials = oAuth2Authentication.getCredentials();
        this.details = oAuth2Authentication.getDetails();
        this.principal = oAuth2Authentication.getPrincipal();
        this.name = oAuth2Authentication.getName();
        this.authorities = new ArrayList<>();
        this.authenticated = oAuth2Authentication.isAuthenticated();
        this.username = username;
        roles.forEach((item) -> this.authorities.add(new SimpleGrantedAuthority(item)));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getDetails() {
        return this.details;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.authenticated = b;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getUsername() { return this.username;}
}
