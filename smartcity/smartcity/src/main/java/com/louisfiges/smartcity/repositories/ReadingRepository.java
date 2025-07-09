package com.louisfiges.smartcity.repositories;

import com.louisfiges.smartcity.daos.ReadingDAO;
import com.louisfiges.smartcity.daos.SubjectDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadingRepository extends JpaRepository<ReadingDAO, UUID> {

    List<ReadingDAO> findByReadingCreatedBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    Optional<ReadingDAO> findTopBySubjectOrderByReadingKwhDesc(SubjectDAO subject);

    Optional<ReadingDAO> findBySubject_SubjectIdAndReadingKwh(UUID subjectId, BigDecimal readingKwh);

    /**
     * Would prefer to do the jpa
     */
    @Query("SELECT MAX(r.readingCreated) FROM ReadingDAO r")
    Optional<LocalDateTime> findMostRecentReadingTime();
}