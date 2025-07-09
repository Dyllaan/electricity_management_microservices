package com.louisfiges.provider.controllers;

import com.louisfiges.common.dtos.subject.CreateSubjectDTO;
import com.louisfiges.common.factories.ResponseEntityFactory;
import com.louisfiges.provider.daos.CustomerDAO;
import com.louisfiges.provider.daos.SourceDAO;
import com.louisfiges.common.http.Response;
import com.louisfiges.provider.daos.SubjectDAO;
import com.louisfiges.common.dtos.subject.DeletedSubjectDTO;
import com.louisfiges.provider.services.CustomerService;
import com.louisfiges.provider.services.SourceService;
import com.louisfiges.provider.services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    private final SubjectService subjectService;
    private final SourceService sourceService;
    private final CustomerService customerService;

    @Autowired
    public SubjectController(SubjectService subjectService, SourceService sourceService, CustomerService customerService) {
        this.subjectService = subjectService;
        this.sourceService = sourceService;
        this.customerService = customerService;
        sourceService.createSources();
    }

    @PostMapping
    public ResponseEntity<Response> createSubject(@RequestBody CreateSubjectDTO createSubjectDTO) {
        String sourceName = createSubjectDTO.source();
        Optional<SourceDAO> source = sourceService.find(sourceName);

        if (!sourceService.exists(sourceName)) {
            logger.info("Attempted to create subject with a non-existent source: " + sourceName);
            return ResponseEntityFactory.create("Source not found", HttpStatus.BAD_REQUEST);
        }

        Optional<CustomerDAO> customer = customerService.find(createSubjectDTO.customerId());

        if(customer.isEmpty()) {
            return ResponseEntityFactory.create("Customer not found", HttpStatus.BAD_REQUEST);
        }

        SubjectDAO subjectDAO = new SubjectDAO(source.get(), customer.get());
        SubjectDAO saved = subjectService.create(subjectDAO);

        return new ResponseEntity<>(saved.toDTO(), HttpStatus.CREATED);
    }

    /**
     * Get a subject by its ID
     * @param subjectId UUID of an existing subject
     * @return ResponseEntity<SubjectDAO>
     */
    @GetMapping("/{subjectId}")
    public ResponseEntity<Response> getSubject(@PathVariable UUID subjectId) {
        Optional<SubjectDAO> subjectOpt = subjectService.find(subjectId);

        if (subjectOpt.isEmpty()) {
            return ResponseEntityFactory.create(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subjectOpt.get().toDTO(), HttpStatus.OK);
    }

    /**
     * Delete a subject by its ID
     * @param subjectId the ID of the subject
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Response> deleteSubject(@PathVariable UUID subjectId) {
        try {
            if(!subjectService.exists(subjectId)) {
                return ResponseEntityFactory.create(HttpStatus.NOT_FOUND);
            }
            subjectService.delete(subjectId);
            return ResponseEntityFactory.create(new DeletedSubjectDTO(subjectId), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntityFactory.create(HttpStatus.NOT_FOUND);
        }
    }

}
