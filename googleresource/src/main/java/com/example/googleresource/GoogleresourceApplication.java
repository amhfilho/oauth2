package com.example.googleresource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@RestController
@EnableResourceServer
public class GoogleresourceApplication {

    public static final String SUCCESS_STRING = "success (id: " + UUID.randomUUID().toString().toUpperCase() + ")";

    public static void main(String[] args) {
		SpringApplication.run(GoogleresourceApplication.class, args);
	}

	@GetMapping("/")
	public String securedCall() {
		return SUCCESS_STRING;
	}

	@GetMapping("/user")
	public CustomAuthentication getUser(Principal principal){
		CustomAuthentication authentication = (CustomAuthentication) principal;
		//Map<String,String> map =  (Map<String,String>)authentication.getDetails();
		return authentication;
	}

	@GetMapping("/principal")
	public Principal getPrincipal(Principal principal){
		return principal;
	}

	@GetMapping("/admin")
	public String admin(){
		return SUCCESS_STRING;
	}

	@GetMapping("/admin/2")
    @Secured("WEBGROUP_ADMIN")
	public String admin2(){
        return SUCCESS_STRING;
    }

}
