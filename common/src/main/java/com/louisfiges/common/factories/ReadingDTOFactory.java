package com.louisfiges.common.factories;

import com.louisfiges.common.dtos.reading.ReadingDTO;
import com.louisfiges.common.dtos.reading.ReadingWithSourceDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReadingDTOFactory {

    public static ReadingDTO create(UUID readingId, BigDecimal readingKwh, LocalDateTime readingCreated) {
        return new ReadingDTO(readingId, readingKwh, readingCreated);
    }

    public static ReadingDTO create(UUID subjectId, BigDecimal readingKwh) {
        return new ReadingDTO(subjectId, readingKwh, LocalDateTime.now());
    }

    public static ReadingDTO incrementReading(ReadingDTO latestReading) {
        return ReadingDTOFactory.create(latestReading.getSubjectId(), latestReading.getReadingKwh().add(randomIncrement()), latestReading.getReadingCreated());
    }

    public static BigDecimal randomIncrement() {
        return BigDecimal.valueOf(1 + Math.random() * 10);
    }
}
