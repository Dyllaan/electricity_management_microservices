package com.louisfiges.provider.daos;

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
public class ReadingDAO implements DAO {

    private @Id
    @GeneratedValue(strategy=GenerationType.AUTO) UUID readingId;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectDAO subject;

    @Column(name = "reading_kwh", nullable = false)
    private BigDecimal readingKwh;

    @Column(name = "reading_created")
    private LocalDateTime readingCreated;

    public ReadingDAO() {
    }

    public ReadingDAO(SubjectDAO subject, BigDecimal readingKwh) {
        this.subject = subject;
        this.readingKwh = readingKwh;
        this.readingCreated = LocalDateTime.now();
    }

    public ReadingDAO(UUID readingId, SubjectDAO subject, BigDecimal readingKwh, LocalDateTime readingCreated) {
        this.readingId = readingId;
        this.subject = subject;
        this.readingKwh = readingKwh;
        this.readingCreated = readingCreated;
    }

    public ReadingDAO(SubjectDAO subject, BigDecimal readingKwh, LocalDateTime readingCreated) {
        this.subject = subject;
        this.readingKwh = readingKwh;
        this.readingCreated = readingCreated;
    }

    public BigDecimal getReadingKwh() {
        return readingKwh;
    }

    public LocalDateTime getReadingCreated() {
        return readingCreated;
    }

    public UUID getReadingId() {
        return readingId;
    }

    @Override
    public ReadingDTO toDTO() {
        return ReadingDTOFactory.create(subject.getSubjectId(), readingKwh, readingCreated);
    }

    public ReadingWithSourceDTO toWithSourceDTO() {
        return new ReadingWithSourceDTO(this.readingId, subject.getSubjectId(), readingKwh, readingCreated, subject.getSource().getSourceId());
    }
}
