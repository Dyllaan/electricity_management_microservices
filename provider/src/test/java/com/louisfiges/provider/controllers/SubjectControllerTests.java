package com.louisfiges.provider.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.louisfiges.common.dtos.subject.CreateSubjectDTO;
import com.louisfiges.provider.daos.CustomerDAO;
import com.louisfiges.provider.daos.SourceDAO;
import com.louisfiges.provider.daos.SubjectDAO;
import com.louisfiges.provider.services.CustomerService;
import com.louisfiges.provider.services.ReadingService;
import com.louisfiges.provider.services.SourceService;
import com.louisfiges.provider.services.SubjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the SubjectController.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SubjectControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SubjectService subjectService;
    private final SourceService sourceService;
    private final CustomerService customerService;

    @Autowired
    public SubjectControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, SubjectService subjectService, SourceService sourceService, ReadingService readingService, CustomerService customerService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.subjectService = subjectService;
        this.sourceService = sourceService;
        this.customerService = customerService;
    }

    private CustomerDAO customerDAO;
    private SubjectDAO subjectDAO;

    @BeforeEach
    public void setup() {
        customerDAO = new CustomerDAO("Louis", "Figes");
        customerService.create(customerDAO);
        SourceDAO sourceDAO = sourceService.createOrGetManualSource();
        subjectDAO = new SubjectDAO(sourceDAO, customerDAO);
        subjectDAO = subjectService.create(subjectDAO);
    }

    @AfterEach
    public void tearDown() {
        subjectService.delete(subjectDAO.getSubjectId());
        customerService.delete(customerDAO.getCustomerId());
    }

    /**
     * Test that creating a subject returns a 201 Created response
     * compare against the SubjectStatus object returned in the response
     * @throws Exception
     */
    @Test
    public void testCreateSubject() throws Exception {
        String sourceName = "manual";

        CreateSubjectDTO subject = new CreateSubjectDTO(sourceName, customerDAO.getCustomerId());

        mockMvc.perform(post("/subject")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(subject)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subjectId").isNotEmpty())
                .andExpect(jsonPath("$.sourceName").value(sourceName))
                .andExpect(jsonPath("$.subjectAdded").isNotEmpty())
                .andExpect(jsonPath("$.customerId").value(customerDAO.getCustomerId()));

        Optional<SubjectDAO> savedSubject = subjectService.find(subjectDAO.getSubjectId());
        assertTrue(savedSubject.isPresent(), "Subject should be saved in the Service");
    }

    /**
     * Test that getting a valid subject returns a 200 OK response
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSubject_Success() throws Exception {
        mockMvc.perform(get("/subject/{subjectId}", subjectDAO.getSubjectId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subjectId").value(subjectDAO.getSubjectId().toString()))
                .andExpect(jsonPath("$.sourceName").value("manual"))
                .andExpect(jsonPath("$.subjectAdded").isNotEmpty())
                .andExpect(jsonPath("$.customerId").value(customerDAO.getCustomerId()));
    }

    /**
     * Test that getting a subject that does not exist returns a 404 Not Found response
     * @throws Exception if the test fails
     */
    @Test
    public void testGetSubjectNotFound() throws Exception {
        mockMvc.perform(get("/subject/{subjectId}", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Test that deleting a subject returns a 204 No Content response
     * @throws Exception if the test fails
     */
    @Test
    public void testDeleteSubject_Success() throws Exception {
        mockMvc.perform(delete("/subject/{subjectId}", subjectDAO.getSubjectId().toString()))
                .andExpect(status().isNoContent());

        Optional<SubjectDAO> deletedSubject = subjectService.find(subjectDAO.getSubjectId());
        assertFalse(deletedSubject.isPresent(), "Subject should be deleted from the Service");
    }

    /**
     * Test that deleting a subject that does not exist returns a 404 Not Found response
     * @throws Exception if the test fails
     */
    @Test
    public void testDeleteSubjectNotFound() throws Exception {
        mockMvc.perform(delete("/subject/{subjectId}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

}
