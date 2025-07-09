package com.louisfiges.smartcity.services;

import com.louisfiges.common.factories.ResponseEntityFactory;
import com.louisfiges.common.http.ServiceInterface;
import com.louisfiges.smartcity.daos.ProviderDAO;
import com.louisfiges.smartcity.daos.SourceDAO;
import com.louisfiges.smartcity.daos.SubjectDAO;
import com.louisfiges.smartcity.repositories.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubjectService implements ServiceInterface<UUID, SubjectDAO> {

    private final SubjectRepository subjectRepository;
    private final SourceService sourceService;
    private final ProviderService providerService;

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, SourceService sourceService, ProviderService providerService) {
        this.subjectRepository = subjectRepository;
        this.providerService = providerService;
        this.sourceService = sourceService;
    }

    @Override
    public SubjectDAO create(SubjectDAO o) {
        return subjectRepository.save(o);
    }

    @Override
    public void delete(UUID subjectId) {
        try {
            subjectRepository.deleteById(subjectId);
        } catch (Exception e) {
            logger.error("Could not delete subject with id: {}", subjectId);
            throw new IllegalArgumentException("Subject not found");
        }
    }

    @Override
    public SubjectDAO read(UUID subjectId) {
        Optional<SubjectDAO> subject = subjectRepository.findById(subjectId);
        if (subject.isEmpty()) {
            logger.error("Subject not found: {}", subjectId);
            throw new IllegalArgumentException("Subject not found");
        }
        return subject.get();
    }

    @Override
    public SubjectDAO update(SubjectDAO o) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<SubjectDAO> find(UUID subjectId) {
        return subjectRepository.findById(subjectId);
    }

    @Override
    public boolean exists(UUID subjectId) {
        return subjectRepository.existsById(subjectId);
    }

    public SubjectDAO getOrCreate(UUID subjectId, String providerUri, String sourceName) {
        try {
            Optional<ProviderDAO> provD = providerService.find(providerUri);
            if (provD.isEmpty()) {
                throw new IllegalArgumentException("Provider not found");
            }
            ProviderDAO provider = provD.get();

            Optional<SubjectDAO> subject = subjectRepository.findById(subjectId);

            Optional<SourceDAO> source = sourceService.find(sourceName);

            if (!sourceService.exists(sourceName)) {
                throw new IllegalArgumentException("Source not found");
            }

            if (subject.isPresent()) {
                if (!subject.get().getSource().getSourceId().equals(source.get().getSourceId())) {
                    throw new IllegalArgumentException("Subject does not belong to source " + sourceName);
                }
                return subject.get();
            }

            return subject.orElseGet(() -> create(new SubjectDAO(subjectId, source.get(), provider)));
        } catch (IllegalArgumentException e) {
            logger.error("Could not create subject: {}", e.getMessage());
            return null;
        }
    }

    public List<SubjectDAO> findAll() {
        return subjectRepository.findAll();
    }

}
