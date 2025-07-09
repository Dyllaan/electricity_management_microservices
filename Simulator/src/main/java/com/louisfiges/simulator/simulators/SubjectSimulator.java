package com.louisfiges.simulator.simulators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.louisfiges.common.dtos.customer.CustomerDTO;
import com.louisfiges.common.dtos.subject.CreateSubjectDTO;
import com.louisfiges.common.dtos.subject.SubjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class SubjectSimulator {

    private String providerUri;
    private RestTemplate restTemplate;
    private Logger logger = LoggerFactory.getLogger(SubjectSimulator.class);

    private final String gatewayUrl;

    public SubjectSimulator(String providerUri, String gatewayUrl) {
        this.providerUri = providerUri;
        this.gatewayUrl = gatewayUrl;
        this.restTemplate = new RestTemplate();
    }

    public List<CreateSubjectDTO> createSubjects(String sourceName, List<CustomerDTO> customers) {
        return customers.stream().map(customer -> new CreateSubjectDTO(sourceName, customer.customerId())).collect(Collectors.toList());
    }

    public List<SubjectDTO> sendSubjects(List<CreateSubjectDTO> subjects) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        String url = gatewayUrl + providerUri + "/subject";

        return subjects.stream()
                .map(subject -> {
                    try {
                        ResponseEntity<String> response = restTemplate.postForEntity(url, subject, String.class);
                        return objectMapper.readValue(response.getBody(), SubjectDTO.class);
                    } catch (Exception e) {
                        logger.error("Error sending Subject to provider: {}", e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull).collect(Collectors.toList());
    }
}
