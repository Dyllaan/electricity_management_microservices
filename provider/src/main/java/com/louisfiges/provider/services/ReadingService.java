package com.louisfiges.provider.services;

import com.louisfiges.common.http.ServiceInterface;
import com.louisfiges.common.dtos.reading.ReadingDTO;
import com.louisfiges.common.dtos.reading.ReadingWithSourceDTO;
import com.louisfiges.common.dtos.status.ValidationStatus;
import com.louisfiges.common.factories.ReadingDTOFactory;
import com.louisfiges.common.factories.ValidationStatusFactory;
import com.louisfiges.provider.daos.ReadingDAO;
import com.louisfiges.provider.daos.SubjectDAO;
import com.louisfiges.common.dtos.reading.RangeDTO;
import com.louisfiges.provider.repositories.ReadingRepository;
import com.louisfiges.provider.repositories.SubjectRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReadingService implements ServiceInterface<UUID, ReadingDAO> {

    private static final Logger logger = LoggerFactory.getLogger(ReadingService.class);

    private final ReadingRepository readingRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public ReadingService(ReadingRepository readingRepository, SubjectRepository subjectRepository) {
        this.readingRepository = readingRepository;
        this.subjectRepository = subjectRepository;
    }

    /**
     * Save a reading to the database
     * @param o a ReadingDAO object of the reading data
     * readingCreated isn't required, if not provided will default to current time
     */
    @Override
    public ReadingDAO create(ReadingDAO o) {
        return readingRepository.save(o);
    }

    @Override
    public ReadingDAO read(UUID id) {
        return readingRepository.findById(id).orElse(null);
    }

    @Override
    public ReadingDAO update(ReadingDAO o) {
        throw new UnsupportedOperationException("Update not supported for readings");
    }

    @Override
    public void delete(UUID id) {
        throw new UnsupportedOperationException("Delete not supported for readings");
    }

    /**
     * Check if the reading is valid by comparing it to the last reading
     * Needs to be greater than the last reading
     * will assume 0 if no previous reading
     * @param subject the subject object to check the reading against
     * @param readingDAO the reading data to validate
     * @return boolean
     */
    @Transactional
    public ValidationStatus isReadingValid(SubjectDAO subject, ReadingDAO readingDAO) {
        logger.info("Validating reading: " + readingDAO.toString());
        Optional<ReadingDAO> latestReadingOpt = readingRepository.findTopBySubjectOrderByReadingKwhDesc(subject);

        logger.info("Latest reading: " + latestReadingOpt.toString());
        logger.info("Found last readings: " + latestReadingOpt.isEmpty());

        if (latestReadingOpt.isPresent()) {
            ReadingDAO latestReading = latestReadingOpt.get();
            logger.info("prev + " + latestReading.getReadingKwh());

            if (readingDAO.getReadingKwh().compareTo(latestReading.getReadingKwh()) <= 0) {
                logger.error("Reading must be greater than the last reading");
                return ValidationStatusFactory.invalid("Reading must be greater than the last reading");
            }

            if (readingDAO.getReadingCreated().isBefore(latestReading.getReadingCreated())) {
                logger.error("Reading must be after the last reading");
                return ValidationStatusFactory.invalid("Reading must be after the last reading");
            }
        }

        if (readingDAO.getReadingKwh().compareTo(BigDecimal.ZERO) < 0) {
            logger.error("Reading must be greater than or equal to 0");
            return ValidationStatusFactory.invalid("Reading must be greater than or equal to 0");
        }

        return ValidationStatusFactory.valid();
    }


    /**
     * Called by Citizen microservice  to simulate valid readings
     * @return a list of reading dtos of the latest readings for all meters in the db
     */
    public List<ReadingDTO> getLatestReadings() {
        List<SubjectDAO> subjects = subjectRepository.findAll();

        return subjects.stream()
                .map(subject -> {
                    ReadingDAO latestReading = subject.getLatestReading();
                    BigDecimal readingKwh = (latestReading != null) ? latestReading.getReadingKwh() : BigDecimal.ZERO;
                    LocalDateTime readingCreated = (latestReading != null) ? latestReading.getReadingCreated() : null;
                    return ReadingDTOFactory.create(subject.getSubjectId(), readingKwh, readingCreated);
                })
                .collect(Collectors.toList());
    }

    /**
     * Use with source dto as this is for the smart city microservice
     * if no end date just get the most recent reading and use that as the end date
     * if no start date use the earliest reading in the db and use that as the start date
     *
     * if no readings then return an empty list cos theres no readings anyways
     *
     * @param startDateTime timestamp no timezone
     * @param endDateTime timestamp no timezone
     * @return return all readings between two days as a list of reading dtos with the source name
     */
    public RangeDTO findByReadingCreatedBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        if(endDateTime == null) {
            Optional<ReadingDAO> rDAO = readingRepository.findFirstByOrderByReadingCreatedDesc();
            if(rDAO.isEmpty()) {
                return null;
            }
            endDateTime = rDAO.get().getReadingCreated();
        }

        if(startDateTime == null) {
            Optional<ReadingDAO> rDAO = readingRepository.findFirstByOrderByReadingCreatedAsc();
            if(rDAO.isEmpty()) {
                return null;
            }
            startDateTime = rDAO.get().getReadingCreated();
        }

        return new RangeDTO(startDateTime, endDateTime, readingRepository.findByReadingCreatedBetween(startDateTime, endDateTime)
                .stream()
                .map(ReadingDAO::toWithSourceDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<ReadingDAO> find(UUID id) {
        return readingRepository.findById(id);
    }

    @Override
    public boolean exists(UUID id) {
        return readingRepository.existsById(id);
    }
}
