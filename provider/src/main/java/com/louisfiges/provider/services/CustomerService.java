package com.louisfiges.provider.services;

import com.louisfiges.common.http.ServiceInterface;
import com.louisfiges.provider.daos.CustomerDAO;
import com.louisfiges.provider.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService implements ServiceInterface<Long, CustomerDAO> {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<CustomerDAO> find(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public boolean exists(Long id) {
        return customerRepository.existsById(id);
    }

    @Override
    public CustomerDAO create(CustomerDAO o) {
        return customerRepository.save(o);
    }

    @Override
    public CustomerDAO read(Long i) {
        return customerRepository.findById(i)
                .orElseThrow();
    }

    @Override
    public CustomerDAO update(CustomerDAO o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long i) {
        customerRepository.deleteById(i);
    }

    /**
     * Name is valid if its not null, more than 2 and less than 30 characters, and is alphbetical
     * with hypens and apostrophes because some names have those characters
     * @param name the name to validate
     * @return true if the name is valid, false otherwise
     */
    public boolean isNameInvalid(String name) {
        if (name == null || name.isEmpty()) {
                return true;
        }

        if (name.length() < 2 || name.length() > 30) {
                return true;
        }

        return !name.matches("[a-zA-Z'\\-]+");
    }
}
