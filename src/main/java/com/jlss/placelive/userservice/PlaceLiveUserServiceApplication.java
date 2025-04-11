package com.jlss.placelive.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jlss.placelive.userservice", "com.jlss.placelive.commonlib"})
public class PlaceLiveUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaceLiveUserServiceApplication.class, args);
	}

}
