package com.louisfiges.citizen.controllers;

import com.louisfiges.citizen.services.ProviderService;
import com.louisfiges.common.factories.ReadingDTOFactory;
import com.louisfiges.common.dtos.reading.CreateReadingDTO;
import com.louisfiges.citizen.services.LatestReadingService;
import com.louisfiges.common.dtos.StringErrorDTO;
import com.louisfiges.common.factories.ResponseEntityFactory;
import com.louisfiges.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reading")
public class LatestReadingController {

    private final LatestReadingService latestReadingService;
    private final ProviderService providerService;

    @Autowired
    public LatestReadingController(LatestReadingService latestReadingService, ProviderService providerService) {
        this.latestReadingService = latestReadingService;
        this.providerService = providerService;
    }
    /**
     * Endpoint to submit a manual reading
     * Will try to submit the reading
     * and return what the provider responds
     *
     * Additionally now it will save the reading to the in memory db
     * to minimise the chattyness of the services
     * @return ResponseEntity<Response>
     */
    @PostMapping
    public ResponseEntity<Response> submitReading(@RequestBody CreateReadingDTO createReadingDTO) {
        try {

            if(!providerService.exists(createReadingDTO.providerUri())) {
                return ResponseEntityFactory.create(HttpStatus.NOT_FOUND);
            }

            ResponseEntity<Response> response = latestReadingService.sendReading(
                    createReadingDTO.providerUri(),
                    ReadingDTOFactory.create(createReadingDTO.subjectId(), createReadingDTO.readingKwh())
                    );

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new StringErrorDTO("Invalid subject ID"));
        }
    }

}
