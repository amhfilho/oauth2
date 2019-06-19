package com.ibm.ksw;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AuthorizeServlet", urlPatterns = "/auth")
public class Authorize extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PropertyReader reader = PropertyReader.init();

        String code = request.getParameter("code");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h2>Authorization</h2>");
        out.println("<ul>");
        out.println("<li><strong>Request:</strong>" + reader.getAuthorizationEndpoint() + "</li>");
        out.println("<li><strong>Response:</strong> " + request.getRequestURI() + "?" + request.getQueryString() + "</li>");
        out.println("</ul>");


        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(reader.accessTokenUri());
        post.addHeader("Accept","application/json");
        post.addHeader("Content-Type","application/x-www-form-urlencoded");

        post.setEntity(new UrlEncodedFormEntity(getUrlParameters(code,reader)));

        HttpResponse tokenResponse = client.execute(post);
        out.println("<h2>Access token</h2>");
        Map<String,Object> tokenMap = getMap(tokenResponse);
        printMap(out, tokenMap);

        //Reading user info from userInfoUri
        String accessToken = tokenMap.get("access_token").toString();
        HttpGet get = new HttpGet(reader.userInfoUri());
        get.addHeader("Authorization", "Bearer " + accessToken);
        HttpResponse userResponse = client.execute(get);

        out.println("<h2>User Information</h2>");
        Map<String,Object> userMap = getMap(userResponse);
        printMap(out, userMap);

        out.println("<a href='/'>Go back</a>");

        request.getSession().setAttribute("name", userMap.get("name"));
        request.getSession().setAttribute("accessToken", tokenMap.get("access_token"));
    }

    private List<NameValuePair> getUrlParameters(String code, PropertyReader reader){
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("code", code));
        urlParameters.add(new BasicNameValuePair("redirect_uri", reader.authorizationCallbackUri()));
        urlParameters.add(new BasicNameValuePair("client_id", reader.clientId()));
        urlParameters.add(new BasicNameValuePair("scope", reader.scope()));
        urlParameters.add(new BasicNameValuePair("client_secret", reader.clientSecret()));
        urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
        return urlParameters;
    }


    private void printMap(PrintWriter out, Map<String,Object> map){
        out.println("<ul>");
        map.forEach((k,v)-> out.println("<li><strong>" + k + ":</strong>" + v + "</li>"));
        out.println("</ul>");
    }

    private Map<String,Object> getMap(HttpResponse response) throws IOException {
        String text = extract(response);
        return new ObjectMapper().readValue(text, HashMap.class);
    }

    private String extract(HttpResponse response) throws IOException {
        InputStream inputStream = response.getEntity().getContent();
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
    }

}
