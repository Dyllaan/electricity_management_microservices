package com.louisfiges.smartcity;

import com.louisfiges.smartcity.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SmartCityApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmartCityApplication.class, args);
	}
}
