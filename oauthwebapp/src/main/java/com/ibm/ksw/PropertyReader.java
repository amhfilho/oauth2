package com.ibm.ksw;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyReader {
    private final static String PROPERTY_FILE = "application.properties";
    public static final String USER_AUTHORIZATION_URI = "userAuthorizationUri";
    public static final String ACCESS_TOKEN_URI = "accessTokenUri";
    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String SCOPE = "scope";
    public static final String AUTHORIZATION_CALLBACK_URI = "authorizationCallbackUri";
    public static final String USER_INFO_URI = "userInfoUri";
    private Properties properties;

    private PropertyReader(Properties properties){
        this.properties = properties;
    }

    public static PropertyReader init(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("application.properties");
        Properties properties = new Properties();
        try {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return new PropertyReader(properties);
    }

    public String authorizationUri(){
        return properties.getProperty(USER_AUTHORIZATION_URI);
    }

    public String accessTokenUri(){
        return properties.getProperty(ACCESS_TOKEN_URI);
    }

    public String clientId(){
        return properties.getProperty(CLIENT_ID);
    }

    public String clientSecret(){
        return properties.getProperty(CLIENT_SECRET);
    }

    public String scope(){
        return properties.getProperty(SCOPE);
    }

    public String authorizationCallbackUri(){
        return properties.getProperty(AUTHORIZATION_CALLBACK_URI);
    }

    public String userInfoUri() { return properties.getProperty(USER_INFO_URI); }

    public String getAuthorizationEndpoint() {
        final Map<String, String> params = new ConcurrentHashMap<>();
        params.put("client_id", Utils.encode(this.clientId()));
        params.put("response_type", "code");
        params.put("redirect_uri", Utils.encode((this.authorizationCallbackUri())));
        params.put("scope", Utils.encode((this.scope())));

        return buildUrl(this.authorizationUri(), params);
    }

    private String buildUrl(String authEndpoint, Map<String, String> params) {
        final List<String> authParams = new ArrayList<>(params.size());

        params.forEach((param, value) -> authParams.add(param + "=" + value));

        return authEndpoint + "?" + authParams.stream()
                .reduce((a,b) -> a + "&" + b).get();
    }



}
