package com.louisfiges.provider.repositories;

import com.louisfiges.provider.daos.ReadingDAO;
import com.louisfiges.provider.daos.SubjectDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadingRepository extends JpaRepository<ReadingDAO, UUID> {

    List<ReadingDAO> findByReadingCreatedBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    Optional<ReadingDAO> findTopBySubjectOrderByReadingKwhDesc(SubjectDAO subject);

    Optional<ReadingDAO> findFirstByOrderByReadingCreatedDesc();

    Optional<ReadingDAO> findFirstByOrderByReadingCreatedAsc();
}