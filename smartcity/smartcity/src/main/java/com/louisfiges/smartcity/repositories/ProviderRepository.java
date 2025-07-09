package com.louisfiges.smartcity.repositories;

import com.louisfiges.smartcity.daos.ProviderDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProviderRepository extends JpaRepository<ProviderDAO, String> {

    @Query("SELECT p.providerId FROM ProviderDAO p")
    List<String> findAllProviderUris();
}