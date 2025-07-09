package com.louisfiges.common.dtos.reading;

import com.louisfiges.common.http.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReadingDTO implements Response {

    private UUID subjectId;
    private BigDecimal readingKwh;
    private LocalDateTime readingCreated;

    public ReadingDTO() {
    }

    public ReadingDTO(UUID subjectId, BigDecimal readingKwh, LocalDateTime readingCreated) {
        this.subjectId = subjectId;
        this.readingKwh = readingKwh;
        this.readingCreated = readingCreated;
    }


    public BigDecimal getReadingKwh() {
        return readingKwh;
    }

    public void setReadingKwh(BigDecimal readingKwh) {
        this.readingKwh = readingKwh;
    }

    public LocalDateTime getReadingCreated() {
        return readingCreated;
    }

    public void setReadingCreated(LocalDateTime readingCreated) {
        this.readingCreated = readingCreated;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }
}
