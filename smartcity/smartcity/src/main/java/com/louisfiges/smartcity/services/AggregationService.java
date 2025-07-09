package com.louisfiges.smartcity.services;

import com.louisfiges.smartcity.aggregate.Aggregator;
import com.louisfiges.smartcity.config.CityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * Every 5 mins makeScheduledRequest will run
 * this will use the aggregator class to asynchronously request providers with a backoff
 * this will not occur if the backoff is already happening
 *
 */
@EnableScheduling
@Service
public class AggregationService {
    private final Logger logger = LoggerFactory.getLogger(com.louisfiges.smartcity.services.AggregationService.class);

    private CityConfig cityConfig;

    private final List<Aggregator> aggregators = new ArrayList<>();

    private final ReadingService readingService;

    @Autowired
    public AggregationService(ProviderService providerService, ReadingService readingService, SubjectService subjectService, SourceService sourceService, CityConfig cityConfig) {
        this.readingService = readingService;
        this.cityConfig = cityConfig;
        providerService.createProviders();
        sourceService.createSources();
        this.makeScheduledRequest();

        String endpoint = "reading/range";
        List<String> providerUrls = Arrays.asList(
                getGatewayUrl() + "/prov-a/" + endpoint,
                getGatewayUrl() + "/prov-b/" + endpoint,
                getGatewayUrl() + "/prov-c/" + endpoint
        );

        for (String url : providerUrls) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
            /**
             * doubled after every failed attempt
             */
            int baseWaitTime = 2000;
            RestTemplate restTemplate = new RestTemplate();
            int maxRetries = 10;
            aggregators.add(new Aggregator(url, scheduler, restTemplate, maxRetries, baseWaitTime, readingService, providerService, subjectService));
        }
    }

    /**
     * CAN BE CALLED VIA POST TO
     * http://localhost:8080/aggregate
     * or via the  scheduled task every 5mins
     */
    @Async
    @Scheduled(fixedRate = 300000)
    public void makeScheduledRequest() {
        logger.info("Making scheduled request");
        LocalDateTime newStart = readingService.getNewestReadingDate();
        String params = "";
        if(newStart != null) {
            params = "?start=" + newStart;
        }

        for (Aggregator agg  : aggregators) {
            agg.startRequest(params);
        }
    }

    protected String getGatewayUrl() {
        return "http://" + cityConfig.getName() + "-api-gateway:8080";
    }
}

