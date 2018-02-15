package com.example.googlegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class GooglegatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GooglegatewayApplication.class, args);
	}
}
