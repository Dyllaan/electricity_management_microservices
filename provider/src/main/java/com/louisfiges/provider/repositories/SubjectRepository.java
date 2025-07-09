package com.louisfiges.provider.repositories;

import com.louisfiges.provider.daos.SubjectDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubjectRepository extends JpaRepository<SubjectDAO, UUID> {
    List<SubjectDAO> findBySource_SourceId(@Param("sourceName") String sourceName);
}
