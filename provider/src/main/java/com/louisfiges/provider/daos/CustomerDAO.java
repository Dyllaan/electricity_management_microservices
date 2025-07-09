package com.louisfiges.provider.daos;

import com.louisfiges.common.dtos.customer.CustomerDTO;
import com.louisfiges.common.http.DAO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The actual person who is the customer of the electricity provider,
 * and who can have multiple subjects.
 * i.e. a person with manual readings and a smart meter or several
 */

@Entity
@Table(name = "customer")
public class CustomerDAO implements DAO {
    private @Id
    @GeneratedValue(strategy = GenerationType.AUTO) Long customerId;

    @Column(name = "customer_first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "customer_last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "customer_added")
    private LocalDateTime customerAdded;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<SubjectDAO> subjects = new HashSet<>();

    public CustomerDAO() { }

    public CustomerDAO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.customerAdded = LocalDateTime.now();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<SubjectDAO> getSubjects() {
        return subjects;
    }

    @Override
    public CustomerDTO toDTO() {
        return new CustomerDTO(customerId, firstName, lastName, customerAdded, subjects.stream()
                .map(SubjectDAO::toNoCustomerDTO)
                .collect(Collectors.toSet()));
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
