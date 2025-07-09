package com.louisfiges.smartcity.aggregate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.louisfiges.common.dtos.reading.RangeDTO;
import com.louisfiges.common.dtos.reading.ReadingWithSourceDTO;
import com.louisfiges.smartcity.daos.ProviderDAO;
import com.louisfiges.smartcity.daos.ReadingDAO;
import com.louisfiges.smartcity.daos.SubjectDAO;
import com.louisfiges.smartcity.services.ProviderService;
import com.louisfiges.smartcity.services.ReadingService;
import com.louisfiges.smartcity.services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */

public class Aggregator {

    private static final Logger logger = LoggerFactory.getLogger(Aggregator.class);

    private final ReadingService readingService;
    private final SubjectService subjectService;
    private final ProviderService providerService;

    private String url;
    private final ScheduledExecutorService scheduler;
    private final RestTemplate restTemplate;
    private final int maxRetries;
    private final int baseWaitTime;

    /**
     * Volatile cos itll be modified by the main thread and read by the scheduler thread
     */
    private volatile boolean isRetrying = false;

    public Aggregator(String url, ScheduledExecutorService scheduler, RestTemplate restTemplate, int maxRetries, int baseWaitTime, ReadingService readingService, ProviderService providerService, SubjectService subjectService) {
        this.url = url;
        this.scheduler = scheduler;
        this.restTemplate = restTemplate;
        this.maxRetries = maxRetries;
        this.baseWaitTime = baseWaitTime;
        this.readingService = readingService;
        this.subjectService = subjectService;
        this.providerService = providerService;
    }

    public void startRequest(String params) {
        if (isRetrying) {
            logger.warn("Already retrying request to {}", url);
            return;
        }
        isRetrying = true;
        retryRequest(0, baseWaitTime, params);
    }

    private void retryRequest(int attempts, int waitTime, String params) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                handleSuccessfulResponse(url + params, response.getBody());
                isRetrying = false;
            } else {
                scheduleRetry(attempts + 1, waitTime, params);
            }
        } catch (Exception e) {
            logger.error("Exception from {}: {}", url, e.getMessage());
            scheduleRetry(attempts + 1, waitTime, params);
        }
    }

    private void scheduleRetry(int attempts, int waitTime, String params) {
        if (attempts < maxRetries) {
            scheduler.schedule(() -> retryRequest(attempts, waitTime * 2, params), waitTime, TimeUnit.MILLISECONDS);
        } else {
            logger.error("Max retries reached for {}", url);
            isRetrying = false;
        }
    }

    private void handleSuccessfulResponse(String url, String responseBody) throws JsonProcessingException {
        if (responseBody == null || responseBody.isEmpty()) {
            logger.error("Response body is null");
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        RangeDTO rangeDTO = objectMapper.readValue(responseBody, RangeDTO.class);
        saveReadings(getProviderFromUrl(url), rangeDTO.readings());
    }


    protected void saveReadings(ProviderDAO provider, List<ReadingWithSourceDTO> subjectReadingDTOs) {
        for (ReadingWithSourceDTO subjectReadingDTO : subjectReadingDTOs) {
            if (readingService.find(subjectReadingDTO.getReadingId()).isPresent()) {
                logger.info("Reading already exists: " + subjectReadingDTO.getSubjectId() + " " + subjectReadingDTO.getReadingKwh());
                continue;
            }

            SubjectDAO subject = subjectService.getOrCreate(subjectReadingDTO.getSubjectId(), provider.getProviderId(), subjectReadingDTO.getSourceName());
            if (subject == null) {
                logger.error("Skipping reading due to null subject: {}", subjectReadingDTO.getSubjectId());
                continue;
            }
            ReadingDAO reading = readingService.dtoToDAO(subjectReadingDTO, subject);
            readingService.create(reading);
        }
    }

    private ProviderDAO getProviderFromUrl(String url) {
        if (url.contains("/prov-a/")) return providerService.find("prov-a").orElse(null);
        if (url.contains("/prov-b/")) return providerService.find("prov-b").orElse(null);
        if (url.contains("/prov-c/")) return providerService.find("prov-c").orElse(null);
        return null;
    }
}
