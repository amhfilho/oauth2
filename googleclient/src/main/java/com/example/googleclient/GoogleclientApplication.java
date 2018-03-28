package com.example.googleclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;

@SpringBootApplication
@RestController
public class GoogleclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoogleclientApplication.class, args);
	}

	@GetMapping("/user")
	public Principal user(Principal user){
		return user;
	}

	@GetMapping("/resource")
	public String getProtectedResource(OAuth2Authentication authentication){
		LinkedHashMap<String, String> details = (LinkedHashMap<String, String>)authentication.getUserAuthentication().getDetails();
		return String.format("%s, you are allowed to access this protected resource", details.get("name"));
	}
}
