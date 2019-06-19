package com.ibm.ksw;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PropertyReader reader = PropertyReader.init();
        response.sendRedirect(getAuthorizationEndpoint(reader));
    }

    private String getAuthorizationEndpoint(PropertyReader reader) {
        final Map<String, String> params = new ConcurrentHashMap<>();
        params.put("client_id", Utils.encode(reader.clientId()));
        params.put("response_type", "code");
        params.put("redirect_uri", Utils.encode((reader.authorizationCallbackUri())));
        params.put("scope", Utils.encode((reader.scope())));

        return buildUrl(reader.authorizationUri(), params);

    }

    private String buildUrl(String authEndpoint, Map<String, String> params) {
        final List<String> authParams = new ArrayList<>(params.size());

        params.forEach((param, value) -> authParams.add(param + "=" + value));

        return authEndpoint + "?" + authParams.stream()
                .reduce((a,b) -> a + "&" + b).get();
    }
}
