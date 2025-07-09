package com.louisfiges.smartcity.repositories;

import com.louisfiges.smartcity.daos.SubjectDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SubjectRepository extends JpaRepository<SubjectDAO, UUID> {
}
