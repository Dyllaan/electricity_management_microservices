package com.louisfiges.common.dtos.reading;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for sending readings to the Smart City microservice
 * this differs from a reading dto in that the reading id is not sent
 * but the subject along with the source name is sent
 * so that smart city can identify the source of the reading
 * this is useful for creating avg usage per subject
 */
public class ReadingWithSourceDTO extends ReadingDTO {

    private final UUID readingId;
    private final String sourceName;

    public ReadingWithSourceDTO(UUID readingId, UUID subjectId, BigDecimal readingKwh, LocalDateTime readingCreated, String sourceName) {
        super(subjectId, readingKwh, readingCreated);
        this.readingId = readingId;
        this.sourceName = sourceName;
    }

    public ReadingWithSourceDTO() {
        this.readingId = null;
        this.sourceName = null;
    }

    public String getSourceName() {
        return sourceName;
    }

    public UUID getReadingId() {
        return readingId;
    }
    
}
