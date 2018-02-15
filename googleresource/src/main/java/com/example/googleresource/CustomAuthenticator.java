package com.example.googleresource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;

import javax.naming.AuthenticationException;
import javax.servlet.*;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by mult-e on 23/07/2017.
 */
public class CustomAuthenticator extends OAuth2AuthenticationManager implements Filter {

    private static Logger logger = LoggerFactory.getLogger(CustomAuthenticator.class);

    private CustomAuthenticationService customAuthenticationService;

    public CustomAuthenticator(CustomAuthenticationService customAuthenticationService){
        this.customAuthenticationService = customAuthenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        return super.authenticate(authentication);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        logger.info("CustomAuthenticator was called");
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        if(authentication instanceof OAuth2Authentication){
            logger.info("authentication instanceof OAuth2Authentication");
            final OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)authentication;
            authentication.setAuthenticated(true);

            if(oAuth2Authentication.getUserAuthentication()!=null){
                final CustomAuthentication customAuthentication = customAuthenticationService.createCustomAuthentication(oAuth2Authentication);
                securityContext.setAuthentication(customAuthentication);
            }

        } else {
            logger.info("authentication NOT instanceof OAuth2Authentication");
            SecurityContextHolder.clearContext();
        }
//        if (authentication instanceof CustomAuthentication) {
//            CustomAuthentication custom = (CustomAuthentication) authentication;
//            logger.info("Found custom authentication: " + custom.getPrincipal());
//            if ("GOOD".equals(custom.getPrincipal())) {
//                authentication.setAuthenticated(true);
//            } else {
//                SecurityContextHolder.clearContext();
//            }
//        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
