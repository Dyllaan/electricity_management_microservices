package com.louisfiges.provider.repositories;

import com.louisfiges.provider.daos.CustomerDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerDAO, Long> {
}
