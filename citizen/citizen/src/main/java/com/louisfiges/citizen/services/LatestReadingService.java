package com.louisfiges.citizen.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.louisfiges.citizen.config.CityConfig;
import com.louisfiges.common.dtos.reading.ReadingDTO;
import com.louisfiges.common.dtos.StringErrorDTO;
import com.louisfiges.common.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class LatestReadingService {

    protected final ProviderService providerService;
    private final RestTemplate restTemplate;
    private CityConfig cityConfig;

    private static final String ENDPOINT = "/reading";
    private static Logger logger = LoggerFactory.getLogger(LatestReadingService.class);

    @Autowired
    public LatestReadingService(ProviderService providerService, RestTemplate restTemplate, CityConfig cityConfig) {
        this.providerService = providerService;
        this.restTemplate = restTemplate;
        this.cityConfig = cityConfig;
        providerService.createProviders();
        logger.info(getGatewayUrl());
    }

    private boolean providerExists(String providerUri) {
        return providerService.exists(providerUri);
    }

    public ResponseEntity<Response> sendReading(String providerUri, ReadingDTO readingDTO) {
        if (!providerExists(providerUri)) {
            return ResponseEntity.badRequest().body(new StringErrorDTO("Provider not found"));
        }

        try {
            ReadingDTO stringReadingDTO = new ReadingDTO(
                    readingDTO.getSubjectId(),
                    readingDTO.getReadingKwh(),
                    readingDTO.getReadingCreated()
            );

            ResponseEntity<String> response = restTemplate.postForEntity(
                    getGatewayUrl() + providerUri + ENDPOINT, stringReadingDTO, String.class
            );

            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            ReadingDTO readingDTOReturned = objectMapper.readValue(response.getBody(), ReadingDTO.class);

            logger.info("Reading sent to provider: " + readingDTOReturned.toString());
            return ResponseEntity.status(response.getStatusCode()).body(readingDTOReturned);

        } catch (HttpStatusCodeException e) {
            logger.error("Error from provider: " + e.getMessage());
            return handleError(e);
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.badRequest().body(new StringErrorDTO("There was a problem with the request."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new StringErrorDTO("An unexpected error occurred"));
        }
    }

    /**
     * Handles the error response from the provider
     * Will return if its client or server error
     *
     * fallback with basic error message
     * @param e the error class i use HttpStatusCode exception as its the parent class of all http exceptions
     * @return ResponseEntity of response for output thru API
     */
    private ResponseEntity<Response> handleError(HttpStatusCodeException e) {
        String responseBody = e.getResponseBodyAsString();

        try {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            StringErrorDTO errorDTO = objectMapper.readValue(responseBody, StringErrorDTO.class);
            return ResponseEntity.status(e.getStatusCode()).body(errorDTO);

        } catch (JsonProcessingException ex) {
            String fallbackMessage = (responseBody != null && !responseBody.isBlank())
                    ? responseBody
                    : "An unexpected error occurred";

            String errorMessage = (e.getStatusCode().is4xxClientError() ? "Client" : "Server")
                    + " error: " + fallbackMessage;

            return ResponseEntity.status(e.getStatusCode()).body(new StringErrorDTO(errorMessage));

        } catch (Exception ex) {
            return ResponseEntity.status(e.getStatusCode()).body(new StringErrorDTO("An unexpected error occurred"));
        }
    }



    protected String getGatewayUrl() {
        return "http://" + cityConfig.getName() + "-api-gateway:8080/";
    }

    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
