package com.louisfiges.smartcity.daos;

import com.louisfiges.common.dtos.subject.SubjectNoCustomerDTO;
import com.louisfiges.common.http.DAO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "subject")
public class SubjectDAO implements DAO {

    @Id
    private UUID subjectId;

    @ManyToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "provider_id", nullable = false)
    private ProviderDAO provider;

    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "source_id", nullable = false)
    private SourceDAO source;

    @Column(name = "subject_added", nullable = false)
    private LocalDateTime subjectAdded;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<ReadingDAO> readings = new HashSet<>();


    public SubjectDAO() {}

    public SubjectDAO(UUID subjectId, SourceDAO source, ProviderDAO provider) {
        this.subjectId = subjectId;
        this.source = source;
        this.provider = provider;
        this.subjectAdded = LocalDateTime.now();
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

    public Set<ReadingDAO> getReadings() {
        return readings;
    }

    public ProviderDAO getProvider() {
        return provider;
    }

    public void setProvider(ProviderDAO provider) {
        this.provider = provider;
    }

    @Override
    public SubjectNoCustomerDTO toDTO() {
        return new SubjectNoCustomerDTO(subjectId, source.getSourceId(), subjectAdded);
    }
}
