package com.louisfiges.provider.repositories;

import com.louisfiges.provider.daos.SourceDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<SourceDAO, String> {
}
