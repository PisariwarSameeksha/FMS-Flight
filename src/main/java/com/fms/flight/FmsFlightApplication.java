package com.fms.flight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FmsFlightApplication {

	public static void main(String[] args) {
		SpringApplication.run(FmsFlightApplication.class, args);
	}

}
