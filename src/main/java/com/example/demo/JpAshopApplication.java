package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;


@SpringBootApplication
public class JpAshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpAshopApplication.class, args);
	}
	
	@Bean
	Hibernate5Module hibernate5Module() {
		return new Hibernate5Module();
	}

}
