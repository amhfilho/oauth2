package com.example.googleresource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by mult-e on 24/07/2017.
 */
public class CustomAuthenticationService {

    public static final String DETAIL_PRINCIPAL_USERNAME = "name";
    private UserRolesService userRolesService;


    public CustomAuthenticationService(final UserRolesService userRolesService){
        this.userRolesService = userRolesService;
    }

    public CustomAuthentication createCustomAuthentication(final OAuth2Authentication oAuth2Authentication){
        return null;
    }
}
