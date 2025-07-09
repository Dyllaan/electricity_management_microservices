package com.louisfiges.provider.services;

import com.louisfiges.common.http.ServiceInterface;
import com.louisfiges.common.SourceDefinitions;
import com.louisfiges.provider.daos.SourceDAO;
import com.louisfiges.provider.repositories.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SourceService implements ServiceInterface<String, SourceDAO> {

    private final SourceRepository sourceRepository;

    @Autowired
    public SourceService(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public void createSources() {
        createOrGetManualSource();
        createOrGetSmartMeterSource();
    }

    public SourceDAO createOrGetManualSource() {
        if (find(SourceDefinitions.MANUAL_SOURCE_NAME).isEmpty()) {
            SourceDAO manualSource = new SourceDAO(SourceDefinitions.MANUAL_SOURCE_NAME);
            return create(manualSource);
        }
        return find(SourceDefinitions.MANUAL_SOURCE_NAME).get();
    }

    public SourceDAO createOrGetSmartMeterSource() {
        if (find(SourceDefinitions.SMART_METER_SOURCE_NAME).isEmpty()) {
            SourceDAO smartMeterSource = new SourceDAO(SourceDefinitions.SMART_METER_SOURCE_NAME);
            return create(smartMeterSource);
        }
        return find(SourceDefinitions.SMART_METER_SOURCE_NAME).get();
    }

    /**
     * @param o  SourceDAO entity ready for persistence
     * @return SourceDAO entity after persistence
     */
    @Override
    public SourceDAO create(SourceDAO o) {
        return sourceRepository.save(o);
    }

    @Override

    public SourceDAO read(String i) {
        throw new UnsupportedOperationException("Cannot read a source by ID");
    }
    @Override
    public SourceDAO update(SourceDAO o) {
        throw new UnsupportedOperationException("Cannot update a source");
    }

    @Override
    public void delete(String i) {
        throw new UnsupportedOperationException("Cannot delete a source");
    }

    @Override
    public Optional<SourceDAO> find(String i) {
        return sourceRepository.findById(i);
    }

    @Override
    public boolean exists (String i) {
        return sourceRepository.existsById(i);
    }
}
