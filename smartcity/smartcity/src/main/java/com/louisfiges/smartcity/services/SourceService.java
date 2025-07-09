package com.louisfiges.smartcity.services;

import com.louisfiges.common.SourceDefinitions;
import com.louisfiges.smartcity.daos.SourceDAO;
import com.louisfiges.smartcity.repositories.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SourceService {

    private final SourceRepository sourceRepository;

    @Autowired
    public SourceService(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public void createSources() {
        createOrGetManualSource();
        createOrGetSmartMeterSource();
    }

    public void createOrGetManualSource() {
        if (find(SourceDefinitions.MANUAL_SOURCE_NAME).isEmpty()) {
            SourceDAO manualSource = new SourceDAO(SourceDefinitions.MANUAL_SOURCE_NAME);
            create(manualSource);
            return;
        }
        find(SourceDefinitions.MANUAL_SOURCE_NAME).get();
    }

    public void createOrGetSmartMeterSource() {
        if (find(SourceDefinitions.SMART_METER_SOURCE_NAME).isEmpty()) {
            SourceDAO smartMeterSource = new SourceDAO(SourceDefinitions.SMART_METER_SOURCE_NAME);
            create(smartMeterSource);
            return;
        }
        find(SourceDefinitions.SMART_METER_SOURCE_NAME).get();
    }

    /**
     * @param o  SourceDAO entity ready for persistence
     * @return SourceDAO entity after persistence
     */
    public SourceDAO create(SourceDAO o) {
        return sourceRepository.save(o);
    }

    public Optional<SourceDAO> find(String i) {
        return sourceRepository.findById(i);
    }

    public boolean exists (String i) {
        return sourceRepository.existsById(i);
    }
}
