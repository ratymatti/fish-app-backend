package com.of.fishapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.AllArgsConstructor;

@SpringBootApplication @AllArgsConstructor
public class FishAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FishAppApplication.class, args);
	}

}
