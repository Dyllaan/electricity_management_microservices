package com.louisfiges.provider.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.louisfiges.common.dtos.reading.ReadingDTO;
import com.louisfiges.common.factories.ReadingDTOFactory;
import com.louisfiges.provider.daos.CustomerDAO;
import com.louisfiges.provider.daos.ReadingDAO;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the ReadingController.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ReadingControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ReadingService readingService;
    private final SubjectService subjectService;
    private final SourceService sourceService;
    private final CustomerService customerService;

    @Autowired
    public ReadingControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, SubjectService subjectService, SourceService sourceService, ReadingService readingService, CustomerService customerService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.subjectService = subjectService;
        this.sourceService = sourceService;
        this.readingService = readingService;
        this.customerService = customerService;
    }

    private CustomerDAO customerDAO;
    private SubjectDAO subjectDAO;

    /**
     * Create a customer and a subject before each test
     */
    @BeforeEach
    public void setup() {
        customerDAO = new CustomerDAO("Louis", "Figes");
        customerService.create(customerDAO);
        SourceDAO sourceDAO = sourceService.createOrGetManualSource();
        subjectDAO = new SubjectDAO(sourceDAO, customerDAO);
        subjectDAO = subjectService.create(subjectDAO);
    }

    /**
     * Delete the created customer and subject after each test
     */
    @AfterEach
    public void tearDown() {
        subjectService.delete(subjectDAO.getSubjectId());
        customerService.delete(customerDAO.getCustomerId());
    }

    /**
     * Test that the endpoint returns the reading created with the correct values
     * including the time of creation
     * @throws Exception if the test fails
     */
    @Test
    public void testCreateReading_Success() throws Exception {
        ReadingDTO readingDTO = ReadingDTOFactory.create(subjectDAO.getSubjectId(), BigDecimal.valueOf(100), LocalDateTime.now());
        String expectedTimestamp = readingDTO.getReadingCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        mockMvc.perform(post("/reading")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readingDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subjectId").value(readingDTO.getSubjectId().toString()))
                .andExpect(jsonPath("$.readingKwh").value(readingDTO.getReadingKwh()))
                .andExpect(jsonPath("$.readingCreated").value(expectedTimestamp));
    }

    /**
     * Test that the endpoint returns a 422 Unprocessable Entity status code
     * @throws Exception if the test fails
     */
    @Test
    public void testLowerThanPrevious_Fail() throws Exception {
        ReadingDTO readingDTO = ReadingDTOFactory.create(subjectDAO.getSubjectId(), BigDecimal.valueOf(100), LocalDateTime.now());
        readingService.create(new ReadingDAO(subjectDAO, BigDecimal.valueOf(200), LocalDateTime.now().minusDays(1)));

        mockMvc.perform(post("/reading")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readingDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cause").value("Reading must be greater than the last reading"));
    }

    /**
     * Test that the endpoint returns a 422 Unprocessable Entity status code
     * @throws Exception if the test fails
     */
    @Test
    public void testReadingBeforePrevious_Fail() throws Exception {
        ReadingDTO readingDTO = ReadingDTOFactory.create(subjectDAO.getSubjectId(), BigDecimal.valueOf(100), LocalDateTime.now());
        readingService.create(new ReadingDAO(subjectDAO, BigDecimal.valueOf(50), LocalDateTime.now().plusDays(1)));

        mockMvc.perform(post("/reading")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readingDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cause").value("Reading must be after the last reading"));
    }

    /**
     * Test that the endpoint returns a 422 Unprocessable Entity status code
     * @throws Exception if the test fails
     */
    @Test
    public void testNegativeReading_Fail() throws Exception {
        ReadingDTO readingDTO = ReadingDTOFactory.create(subjectDAO.getSubjectId(), BigDecimal.valueOf(-100), LocalDateTime.now());

        mockMvc.perform(post("/reading")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readingDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cause").value("Reading must be greater than or equal to 0"));
    }

}
