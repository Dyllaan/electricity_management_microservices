package com.louisfiges.smartcity.controllers;

import com.louisfiges.common.dtos.reading.ReadingWithSourceDTO;
import com.louisfiges.common.dtos.subject.SubjectNoCustomerDTO;
import com.louisfiges.smartcity.daos.ReadingDAO;
import com.louisfiges.smartcity.daos.SubjectDAO;
import com.louisfiges.smartcity.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectNoCustomerDTO>> getAllSubjects() {
        return new ResponseEntity<>(subjectService.findAll().stream().map(SubjectDAO::toDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get all readings for a subject
     * @param subjectId the id of the subject
     * @return a list of readings
     */
    @GetMapping("/{subjectId}")
    public ResponseEntity<List<ReadingWithSourceDTO>> getSubjectReadings(@PathVariable UUID subjectId) {
        if(!subjectService.exists(subjectId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subjectService.find(subjectId).get().getReadings().stream().map(ReadingDAO::toWithSourceDTO).collect(Collectors.toList()), HttpStatus.OK);
    }

}
