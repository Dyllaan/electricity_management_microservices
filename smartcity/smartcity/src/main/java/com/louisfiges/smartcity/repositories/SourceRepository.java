package com.louisfiges.smartcity.repositories;

import com.louisfiges.smartcity.daos.SourceDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<SourceDAO, String> {
}
