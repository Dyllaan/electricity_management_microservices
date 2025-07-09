package com.louisfiges.simulator.simulators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.louisfiges.common.dtos.customer.CreateCustomerDTO;
import com.louisfiges.common.dtos.customer.CustomerDTO;
import com.louisfiges.simulator.config.CityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class CustomerSimulator {

    private final String providerUri;
    private final RestTemplate restTemplate;
    private final String gatewayUrl;
    private final Logger logger = LoggerFactory.getLogger(CustomerSimulator.class);

    public CustomerSimulator(String providerUri, String gatewayUrl) {
        this.providerUri = providerUri;
        this.gatewayUrl = gatewayUrl;
        this.restTemplate = new RestTemplate();
    }

    public List<CreateCustomerDTO> createCustomers(int numberOfCustomers) {
        List<CreateCustomerDTO> customers = new ArrayList<>();
        for (int i = 0; i < numberOfCustomers; i++) {
            customers.add(new CreateCustomerDTO("Customer", generateRandomSuffix()));
        }
        return customers;
    }

    public List<CustomerDTO> sendCustomers(List<CreateCustomerDTO> customers) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        String url = gatewayUrl + providerUri + "/customer";

        return customers.stream()
                .map(customer -> {
                    try {
                        ResponseEntity<String> response = restTemplate.postForEntity(url, customer, String.class);
                        return objectMapper.readValue(response.getBody(), CustomerDTO.class);
                    } catch (Exception e) {
                        logger.error("Error sending customer to provider: {}", e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private String generateRandomSuffix() {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            result.append(characters.charAt(new Random().nextInt(characters.length())));
        }
        return result.toString();
    }
}
