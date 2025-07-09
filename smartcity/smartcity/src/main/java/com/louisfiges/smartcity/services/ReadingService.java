package com.louisfiges.smartcity.services;

import com.louisfiges.common.dtos.reading.ReadingWithSourceDTO;
import com.louisfiges.smartcity.daos.ReadingDAO;
import com.louisfiges.smartcity.daos.SubjectDAO;
import com.louisfiges.smartcity.repositories.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReadingService {

    private final ReadingRepository readingRepository;

    @Autowired
    public ReadingService(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    /**
     * Save a reading to the database
     *
     * @param o a ReadingDAO object of the reading data
     *          readingCreated isn't required, if not provided will default to current time
     */
    public void create(ReadingDAO o) {
        readingRepository.save(o);
    }

    public void delete(UUID id) {
        throw new UnsupportedOperationException("Delete not supported for readings");
    }


    public Optional<ReadingDAO> find(UUID id) {
        return readingRepository.findById(id);
    }

    public List<ReadingDAO> getAllReadings(String order) {
        List<ReadingDAO> readings = readingRepository.findAll();
        if ("desc".equalsIgnoreCase(order)) {
            readings.sort(Comparator.comparing(ReadingDAO::getReadingCreated).reversed());
        } else {
            readings.sort(Comparator.comparing(ReadingDAO::getReadingCreated));
        }
        return readings;
    }

    public List<ReadingDAO> getReadingsAbove(double threshold) {
        return readingRepository.findAll().stream()
                .filter(reading -> reading.getReadingKwh().doubleValue() > threshold)
                .collect(Collectors.toList());
    }

    public List<ReadingDAO> getReadingsBelow(double threshold) {
        return readingRepository.findAll().stream()
                .filter(reading -> reading.getReadingKwh().doubleValue() < threshold)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getAverageReadings() {
        List<BigDecimal> readings = readingRepository.findAll().stream()
                .map(ReadingDAO::getReadingKwh)
                .collect(Collectors.toList());

        double mean = calculateMean(readings);
        double median = calculateMedian(readings);
        double mode = calculateMode(readings);

        Map<String, Double> averages = new HashMap<>();
        averages.put("mean", mean);
        averages.put("median", median);
        averages.put("mode", mode);
        return averages;
    }

    private double calculateMean(List<BigDecimal> readings) {
        return readings.stream()
                .mapToDouble(BigDecimal::doubleValue)
                .average()
                .orElse(0.0);
    }

    private double calculateMedian(List<BigDecimal> readings) {
        List<Double> sorted = readings.stream()
                .map(BigDecimal::doubleValue)
                .sorted()
                .toList();
        int size = sorted.size();
        if (size == 0) return 0.0;
        return size % 2 == 0 ?
                (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2 :
                sorted.get(size / 2);
    }

    private double calculateMode(List<BigDecimal> readings) {
        Map<Double, Long> frequencyMap = readings.stream()
                .map(BigDecimal::doubleValue)
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()));

        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0.0);
    }

    public ReadingDAO dtoToDAO(ReadingWithSourceDTO readingSource, SubjectDAO subject) {
        return new ReadingDAO(readingSource.getReadingId(), subject, readingSource.getReadingKwh(), readingSource.getReadingCreated());
    }

    public LocalDateTime getNewestReadingDate() {
        return readingRepository.findMostRecentReadingTime().orElse(null);
    }
}
