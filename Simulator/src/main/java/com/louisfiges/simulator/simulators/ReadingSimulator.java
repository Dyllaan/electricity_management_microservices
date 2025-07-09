package com.louisfiges.simulator.simulators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.louisfiges.common.dtos.customer.CustomerDTO;
import com.louisfiges.common.dtos.simulation.CreateSimulationDTO;
import com.louisfiges.common.dtos.simulation.SimulationStatus;
import com.louisfiges.common.dtos.subject.CreateSubjectDTO;
import com.louisfiges.common.dtos.subject.SubjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReadingSimulator {

    private RestTemplate restTemplate;
    private Logger logger = LoggerFactory.getLogger(ReadingSimulator.class);
    private final String gatewayUrl;

    public ReadingSimulator(String gatewayUrl) {
        this.restTemplate = new RestTemplate();
        this.gatewayUrl = gatewayUrl;
    }

    public SimulationStatus sendSimulationRequest() {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        String url = gatewayUrl + "citizen/simulation";
        CreateSimulationDTO createSimulationDTO = new CreateSimulationDTO(LocalDateTime.now(), LocalDateTime.now().plusYears(1));
        try {
            logger.info("Attempting simulation request to citizen");
            ResponseEntity<String> response = restTemplate.postForEntity(url, createSimulationDTO, String.class);
            return objectMapper.readValue(response.getBody(), SimulationStatus.class);
        } catch (Exception e) {
            logger.error("Error requesting simulation of readings from citizen: {}", e.getMessage());
            return null;
        }
    }
}
