package com.louisfiges.citizen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Async to allow to respond to simulation request and avoid timeout
 */
@EnableAsync
@SpringBootApplication
public class CitizenApplication  {

	public static void main(String[] args) {
		SpringApplication.run(CitizenApplication.class, args);
	}

}
