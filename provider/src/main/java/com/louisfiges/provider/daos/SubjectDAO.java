package com.louisfiges.provider.daos;

import com.louisfiges.common.dtos.subject.SubjectDTO;
import com.louisfiges.common.dtos.subject.SubjectNoCustomerDTO;
import com.louisfiges.common.http.DAO;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "subject")
public class SubjectDAO implements DAO {

    private @Id
    @GeneratedValue UUID subjectId;

    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "source_id", nullable = false)
    private SourceDAO source;

    @Column(name = "subject_created")
    private LocalDateTime subjectAdded;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<ReadingDAO> readings = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerDAO customer;

    public SubjectDAO() {}

    public SubjectDAO(SourceDAO source, CustomerDAO customer) {
        this.source = source;
        this.customer = customer;
        this.subjectAdded = LocalDateTime.now();
        customer.getSubjects().add(this);
    }

    public UUID getSubjectId() {
        return subjectId;
    }


    public SourceDAO getSource() {
        return source;
    }

    public void setSource(SourceDAO source) {
        this.source = source;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public LocalDateTime getSubjectAdded() {
        return subjectAdded;
    }

    public void setSubjectAdded(LocalDateTime subjectAdded) {
        this.subjectAdded = subjectAdded;
    }

    public CustomerDAO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDAO customer) {
        this.customer = customer;
    }

    @Transactional
    public ReadingDAO getLatestReading() {
        return readings.stream()
                .max(Comparator.comparing(ReadingDAO::getReadingCreated))
                .orElse(null);
    }

    public SubjectNoCustomerDTO toNoCustomerDTO() {
        return new SubjectNoCustomerDTO(subjectId, source.getSourceId(), subjectAdded);
    }

    @Override
    public SubjectDTO toDTO() {
        return new SubjectDTO(subjectId, source.getSourceId(), subjectAdded, customer.getCustomerId());
    }
}
