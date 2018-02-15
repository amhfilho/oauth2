package com.example.googleresource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.Collection;

/**
 * Created by mult-e on 23/07/2017.
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {

    private CustomAuthenticator customAuthenticator =
            new CustomAuthenticator(new CustomAuthenticationService(new MemoryUserRolesService()));
    //private static final Logger logger = LoggerFactory.getLogger(OAuth2ResourceConfig.class.getName());

    @Value("${security.oauth2.resource.userInfoUri}")
    private String userInfoUrl;

    @Value("${tokenInfoUri}")
    private String tokenInfoUrl;

    @Value("${clientId}")
    private String clientId;


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        //resources.authenticationManager(customAuthenticator);
        resources.tokenServices(myUserInfoTokenServices());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

//            http.authorizeRequests().anyRequest().authenticated().and()
//                    .addFilterAfter(customAuthenticator, AbstractPreAuthenticatedProcessingFilter.class);

       http.authorizeRequests().anyRequest().authenticated();
//                .and().addFilterAfter(customAuthenticator, AbstractPreAuthenticatedProcessingFilter.class);
//        http.antMatcher("/**")
//                .authorizeRequests()
//                .antMatchers("/admin", "/admin/**").hasAnyRole("WEBGROUP_ADMIN")
//                .anyRequest()
//                .authenticated()
//                .and()
//                .addFilterAfter(customAuthenticator, AbstractPreAuthenticatedProcessingFilter.class)
//                .csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Bean
    @Primary
    public ResourceServerTokenServices myUserInfoTokenServices() {
        return new CustomResourceServerTokenServices(this.clientId, this.userInfoUrl, this.tokenInfoUrl);
    }
}
