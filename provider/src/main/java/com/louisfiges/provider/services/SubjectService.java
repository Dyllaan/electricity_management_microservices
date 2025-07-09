package com.louisfiges.provider.services;

import com.louisfiges.common.http.ServiceInterface;
import com.louisfiges.provider.daos.SubjectDAO;
import com.louisfiges.provider.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SubjectService implements ServiceInterface<UUID, SubjectDAO> {

    private final SubjectRepository subjectRepository;

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
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

}
