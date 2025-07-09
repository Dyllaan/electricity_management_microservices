package com.louisfiges.citizen.services;

import com.louisfiges.common.dtos.reading.ReadingDTO;
import com.louisfiges.common.factories.ReadingDTOFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import static com.louisfiges.common.factories.ReadingDTOFactory.incrementReading;

@Service
public class SimulationService {

    private final LatestReadingService latestReadingService;
    private final ProviderService providerService;

    private Logger logger = LoggerFactory.getLogger(SimulationService.class);

    @Autowired
    public SimulationService(LatestReadingService latestReadingService, ProviderService providerService) {
        this.latestReadingService = latestReadingService;
        this.providerService = providerService;
    }

    @Async
    public void simulateReadings(LocalDateTime start, LocalDateTime end) {
        providerService.findAllUris().forEach(providerUri -> {
            List<ReadingDTO> initialReadings = getReadings(providerUri);
            if (initialReadings == null) {
                logger.error("No readings fetched from " + providerUri);
                return;
            }

            List<ReadingDTO> generatedReadings = new ArrayList<>();

            for (ReadingDTO initialReading : initialReadings) {
                LocalDateTime initialCreatedDate = initialReading.getReadingCreated();

                if (initialCreatedDate == null) {
                    logger.warn("Reading from provider " + providerUri + " has null 'readingCreated'. Defaulting to the start of the range: " + start);
                    initialCreatedDate = start;
                }

                if (!end.isAfter(initialCreatedDate)) {
                            String warning = "No readings generated for provider " + providerUri +
                                    " as the end date " + end + " is on or before the last reading timestamp " + initialCreatedDate;
                            logger.warn(warning);
                            continue;
                        }


                        LocalDateTime current = start.isAfter(initialCreatedDate)
                                ? start
                                : initialCreatedDate.plusMonths(1);

                        while (!current.isAfter(end)) {
                            ReadingDTO nextReading = incrementReading(initialReading);
                            nextReading.setReadingCreated(current);
                            generatedReadings.add(nextReading);

                            initialReading = nextReading;
                            current = current.plusMonths(1);
                        }
                    }

                    logger.info(generatedReadings.size() + " readings generated for " + providerUri);

                    long successCount = generatedReadings.stream()
                            .filter(reading -> {
                                boolean success = latestReadingService.sendReading(providerUri, reading)
                                        .getStatusCode().is2xxSuccessful();
                                if (!success) {
                                    logger.error("Failed to send reading " + reading + " to " + providerUri);
                                }
                                return success;
                            })
                            .count();

                    logger.info(successCount + " readings successfully sent to " + providerUri);
                });
    }

    /**
     * Retrieve the latest readings from a provider
     * Increment the readingKwh by a random amount
     * Return it as a list of ReadingDTOs
     */
    private List<ReadingDTO> getReadings(String providerUri) {
        try {
            ReadingDTO[] latestReadings = latestReadingService.getRestTemplate().getForObject(
                    latestReadingService.getGatewayUrl() + providerUri + "/reading/latest", ReadingDTO[].class);
            logger.error(latestReadings.length + " readings fetched from " + providerUri);
            return latestReadings == null ? List.of() : incrementReadings(Arrays.asList(latestReadings));
        } catch (HttpServerErrorException e) {
            System.out.println("Failed to fetch latest readings from " + providerUri + ": " + e.getMessage());
            return null;
        }
    }

    private List<ReadingDTO> incrementReadings(List<ReadingDTO> readings) {
        return readings.stream()
                .map(ReadingDTOFactory::incrementReading)
                .collect(Collectors.toList());
    }


}
