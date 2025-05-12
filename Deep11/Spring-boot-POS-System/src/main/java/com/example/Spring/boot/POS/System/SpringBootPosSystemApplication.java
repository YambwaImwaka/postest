package com.example.Spring.boot.POS.System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;

@SpringBootApplication
/*@EnableAutoConfiguration(exclude = {
		ThymeleafAutoConfiguration.class
})*/
public class SpringBootPosSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootPosSystemApplication.class, args);
	}

}
