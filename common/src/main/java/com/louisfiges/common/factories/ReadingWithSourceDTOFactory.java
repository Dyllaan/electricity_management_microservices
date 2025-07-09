package com.louisfiges.common.factories;

import com.louisfiges.common.dtos.reading.ReadingDTO;
import com.louisfiges.common.dtos.reading.ReadingWithSourceDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReadingWithSourceDTOFactory {

    public static ReadingWithSourceDTO create(UUID readingId, UUID subjectId, BigDecimal readingKwh, LocalDateTime readingCreated, String sourceName) {
        return new ReadingWithSourceDTO(readingId, subjectId, readingKwh, readingCreated, sourceName);
    }

    public static ReadingWithSourceDTO create(UUID readingId, UUID subjectId, BigDecimal readingKwh, String sourceName) {
        return new ReadingWithSourceDTO(readingId, subjectId, readingKwh, LocalDateTime.now(), sourceName);
    }
}
