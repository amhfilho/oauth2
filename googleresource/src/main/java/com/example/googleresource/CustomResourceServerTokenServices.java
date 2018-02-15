package com.example.googleresource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.*;

public class CustomResourceServerTokenServices implements ResourceServerTokenServices {

    private static final Logger logger = LoggerFactory.getLogger(CustomResourceServerTokenServices.class.getName());
    public static final String USERNAME = "username";

    private String userInfoUrl;
    private String tokenInfoUrl;
    private String clientId;

    private UserRolesService userRolesService = new MemoryUserRolesService();

    public CustomResourceServerTokenServices(String clientId, String userInfoUrl, String tokenInfoUrl){
        this.clientId = clientId;
        this.userInfoUrl = userInfoUrl;
        this.tokenInfoUrl = tokenInfoUrl;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = getMap(this.userInfoUrl, accessToken);
        if (map.containsKey("error")) {
            this.logger.debug("userinfo returned error: " + map.get("error"));
            this.logger.debug("Trying to call tokeninfo endpoint: " + this.tokenInfoUrl);
            map = getMap(this.tokenInfoUrl, accessToken);
            if(map.containsKey("error")){
                this.logger.debug("tokeninfo returned error: " + map.get("error") + ", could not authenticate");
                throw new InvalidTokenException(accessToken);
            }
        }
        return extractAuthentication(map);
    }



    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    private Map<String, Object> getMap(String path, String accessToken) {
        logger.debug("Getting user info from: " + path);
        try {
            BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
            resource.setClientId(this.clientId);
            OAuth2RestOperations restTemplate = new OAuth2RestTemplate(resource);

            OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext().getAccessToken();
            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
                token.setTokenType(DefaultOAuth2AccessToken.BEARER_TYPE);
                restTemplate.getOAuth2ClientContext().setAccessToken(token);
            }
            Map<String, Object> result = restTemplate.getForEntity(path, Map.class).getBody();
            return result;
        }
        catch (Exception ex) {
            this.logger.warn("Could not fetch user details: " + ex.getClass() + ", "
                        + ex.getMessage());
            return Collections.<String, Object>singletonMap("error","Could not fetch user details");
            }
    }

    private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
        Object principal = this.getPrincipal(map);
        String username = (String)map.get(USERNAME);
        List<GrantedAuthority> authorities = this.userRolesService.loadAuthoritiesFromWebGroups(username);
                //this.authoritiesExtractor.extractAuthorities(map);
        OAuth2Request request = new OAuth2Request((Map)null, this.clientId, (Collection)null, true, (Set)null, (Set)null, (String)null, (Set)null, (Map)null);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        token.setDetails(map);
        return new OAuth2Authentication(request, token);
    }

    protected Object getPrincipal(Map<String, Object> map) {
        Object principal = new FixedPrincipalExtractor().extractPrincipal(map);
        return (principal == null ? "unknown" : principal);

        /*
        Object principal = this.principalExtractor.extractPrincipal(map);
        return (principal == null ? "unknown" : principal);
        */

    }

    public void setUserRolesService(UserRolesService userRolesService){
        this.userRolesService = userRolesService;
    }
}
