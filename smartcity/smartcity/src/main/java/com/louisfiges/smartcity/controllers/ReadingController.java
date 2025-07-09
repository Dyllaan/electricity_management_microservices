package com.louisfiges.smartcity.controllers;

import com.louisfiges.common.dtos.reading.ReadingWithSourceDTO;
import com.louisfiges.smartcity.daos.ReadingDAO;
import com.louisfiges.smartcity.services.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reading")
public class ReadingController {

    private final ReadingService readingService;

    @Autowired
    public ReadingController(ReadingService readingService) {
        this.readingService = readingService;
    }

    @GetMapping
    public ResponseEntity<List<ReadingWithSourceDTO>> getAllReadings(
            @RequestParam(defaultValue = "asc") String order
    ) {
        try {
            List<ReadingDAO> readings = readingService.getAllReadings(order);

            List<ReadingWithSourceDTO> readingDTOs = readings.stream().map(ReadingDAO::toWithSourceDTO).toList();

            return new ResponseEntity<>(readingDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/above")
    public ResponseEntity<List<ReadingWithSourceDTO>> getReadingsAbove(@RequestParam double threshold) {
        try {
            List<ReadingDAO> readings = readingService.getReadingsAbove(threshold);

            List<ReadingWithSourceDTO> readingDTOs = readings.stream().map(ReadingDAO::toWithSourceDTO).toList();

            return new ResponseEntity<>(readingDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/below")
    public ResponseEntity<List<ReadingWithSourceDTO>> getReadingsBelow(@RequestParam double threshold) {
        try {
            List<ReadingDAO> readings = readingService.getReadingsBelow(threshold);

            List<ReadingWithSourceDTO> readingDTOs = readings.stream().map(ReadingDAO::toWithSourceDTO).toList();

            return new ResponseEntity<>(readingDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/average")
    public ResponseEntity<Map<String, Double>> getAverageReadings() {
        try {
            Map<String, Double> averages = readingService.getAverageReadings();
            return new ResponseEntity<>(averages, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
