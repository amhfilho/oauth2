package com.example.resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@EnableResourceServer
@Configuration
public class OAuth2Configuration extends ResourceServerConfigurerAdapter {
    public static final String RESOURCE_ID = "resource";

    public void configure(ResourceServerSecurityConfigurer configurer){
        configurer.resourceId(RESOURCE_ID);
    }

    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and().csrf().disable();

    }
}
