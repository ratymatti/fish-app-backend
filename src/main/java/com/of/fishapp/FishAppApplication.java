package com.of.fishapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.AllArgsConstructor;

@SpringBootApplication @AllArgsConstructor @EntityScan(basePackages = {"com.of.fishapp.entity"}) @EnableJpaRepositories("com.of.fishapp.repository")   
public class FishAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FishAppApplication.class, args);
	}

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
