package com.louisfiges.citizen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CitizenConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
