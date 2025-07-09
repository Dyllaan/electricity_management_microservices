package com.louisfiges.smartcity.daos;

import com.louisfiges.common.dtos.reading.ReadingDTO;
import com.louisfiges.common.dtos.reading.ReadingWithSourceDTO;
import com.louisfiges.common.factories.ReadingDTOFactory;
import com.louisfiges.common.http.DAO;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reading")
public class ReadingDAO {

    @Id
    private UUID readingId;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectDAO subject;

    @Column(name = "reading_kwh", nullable = false)
    private BigDecimal readingKwh;

    @Column(name = "reading_created")
    private LocalDateTime readingCreated;

    public ReadingDAO() {
    }

    public ReadingDAO(UUID readingId, SubjectDAO subject, BigDecimal readingKwh, LocalDateTime readingCreated) {
        this.readingId = readingId;
        this.subject = subject;
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

    public UUID getReadingId() {
        return readingId;
    }

    public void setReadingId(UUID readingId) {
        this.readingId = readingId;
    }

    public ReadingWithSourceDTO toWithSourceDTO() {
        return new ReadingWithSourceDTO(readingId, subject.getSubjectId(), readingKwh, readingCreated, subject.getSource().getSourceId());
    }
}
