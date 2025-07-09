package com.louisfiges.provider.controllers;

import com.louisfiges.common.dtos.StringErrorDTO;
import com.louisfiges.common.dtos.reading.ReadingDTO;
import com.louisfiges.common.dtos.reading.ReadingWithSourceDTO;
import com.louisfiges.common.dtos.status.ValidationStatus;
import com.louisfiges.common.factories.ResponseEntityFactory;
import com.louisfiges.common.factories.StringErrorFactory;
import com.louisfiges.common.http.Response;
import com.louisfiges.provider.daos.ReadingDAO;
import com.louisfiges.provider.daos.SubjectDAO;
import com.louisfiges.provider.services.ReadingService;
import com.louisfiges.provider.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/reading")
public class ReadingController {

    private static final Logger logger = LoggerFactory.getLogger(ReadingController.class);

    private final ReadingService readingService;
    private final SubjectService subjectService;
    @Autowired
    public ReadingController(ReadingService readingService, SubjectService subjectService) {
        this.readingService = readingService;
        this.subjectService = subjectService;
    }

    /**
     * This is called by the Citizen microservice so it can simulate valid smart meter readings.
     */
    @GetMapping("/latest")
    public ResponseEntity<List<ReadingDTO>> getReadings() {
        try {
            List<ReadingDTO> readings = readingService.getLatestReadings();

            return ResponseEntityFactory.create(readings, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Could not get smart meter readings", e);
            return ResponseEntityFactory.create(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Receive a reading from the Citizen microservice
     * @param readingDTO the reading data, this ensures format is correct
     * @return a response entity with a response object if successful of the reading as a ReadingDTO
     * or an error response otherwise
     */
    @PostMapping
    public ResponseEntity<Response> createReading(@RequestBody ReadingDTO readingDTO) {
        logger.info("Creating reading: " + readingDTO.toString());
        try {
            Optional<SubjectDAO> subjectOpt = subjectService.find(readingDTO.getSubjectId());

            if (subjectOpt.isEmpty()) {
                logger.error("Subject not found");
                return ResponseEntityFactory.create("Subject not found", HttpStatus.NOT_FOUND);
            }

            SubjectDAO subject = subjectOpt.get();

            ReadingDAO readingDAO = new ReadingDAO(subject, readingDTO.getReadingKwh(), readingDTO.getReadingCreated());

            ValidationStatus status = readingService.isReadingValid(subject, readingDAO);

            if (status.valid()) {
                logger.info("Reading is valid");
                ReadingDAO saved = readingService.create(readingDAO);
                return ResponseEntityFactory.create(saved.toDTO(), HttpStatus.CREATED);
            }

            return ResponseEntityFactory.create(StringErrorFactory.create(status.cause()), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (HttpMessageNotReadableException | DateTimeParseException | IllegalArgumentException e) {
            return ResponseEntityFactory.create("Verify API documentation", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Could not create reading 500", e);
            return ResponseEntityFactory.create("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET Method
     * to give the smart city api all the readings in a specific time frame
     * @param start timestamp no timezone
     * @param end timestamp no timezone
     */
    @GetMapping("/range")
    public ResponseEntity<?> getReadingsWithinRange(@RequestParam(required = false) String start, @RequestParam(required=false) String end) {
        try {
            LocalDateTime parsedStartDateTime = null;
            LocalDateTime parsedEndDateTime = null;
            if (start != null) {
                parsedStartDateTime = LocalDateTime.parse(start);
            }

            if (end != null) {
                parsedEndDateTime = LocalDateTime.parse(end);
            }


            if (parsedEndDateTime !=null && parsedStartDateTime.isAfter(parsedEndDateTime)) {
                return ResponseEntityFactory.create(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }

            return ResponseEntityFactory.create(readingService.findByReadingCreatedBetween(parsedStartDateTime, parsedEndDateTime), HttpStatus.OK);
        } catch (NullPointerException e) {
            logger.error("Could not parse date", e);
            return ResponseEntityFactory.create(new StringErrorDTO("Date error"), HttpStatus.BAD_REQUEST);
        } catch (DateTimeParseException e) {
            logger.error("Could not parse date", e);
            return ResponseEntityFactory.create(new StringErrorDTO("Could not parse date"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Could not get readings within range", e);
            return ResponseEntityFactory.create(new StringErrorDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
