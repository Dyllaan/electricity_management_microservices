package com.louisfiges.provider.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.louisfiges.common.dtos.customer.CreateCustomerDTO;
import com.louisfiges.provider.daos.CustomerDAO;
import com.louisfiges.provider.services.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the CustomerController.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CustomerControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final CustomerService customerService;
    /**
     * names[0] is first name
     * names[1] is last name
     */
    private final List<String> names;

    private CustomerDAO savedCustomer;

    @Autowired
    public CustomerControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, CustomerService customerService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.customerService = customerService;
        this.names = Arrays.asList("Louis", "Figes");
    }

    /**
     * Delete the saved customer after each test
     * to keep the database clean
     */
    @AfterEach
    public void tearDown() {
        if (savedCustomer != null) {
            customerService.delete(savedCustomer.getCustomerId());
        }
    }


    /**
     * Should succeed with a 201 status code
     * @throws Exception if the test fails which it shouldn't
     */
    @Test
    public void testCreateCustomer_Success() throws Exception {
        CreateCustomerDTO customerDTO = new CreateCustomerDTO(names.get(0), names.get(1));
        MvcResult result = mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").isNumber())
                .andExpect(jsonPath("$.firstName").value(names.get(0)))
                .andExpect(jsonPath("$.lastName").value(names.get(1)))
                .andExpect(jsonPath("$.customerCreated").isNotEmpty())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseBody);

        customerService.delete(jsonNode.get("customerId").asLong());
    }

    /**
     * This will fail due to invalid names
     * @throws Exception if the test fails which it should
     */
    @Test
    public void testCreateCustomer_Failure() throws Exception {
        CreateCustomerDTO customerDTO = new CreateCustomerDTO("L", "jdfkfkflflkfdlkfldkfldkldflkfdlk}~~~@");
        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cause").value("Names must be more than 2 characters, less than 30 characters and alphabetical"));
    }

    /**
     * Tests if a customer at existing id is successfully deleted
     * @throws Exception if the test fails which it shouldn't
     */
    @Test
    public void testDeleteCustomer_Success() throws Exception {
        savedCustomer = customerService.create(new CustomerDAO(names.get(0), names.get(1)));
        mockMvc.perform(delete("/customer/" + savedCustomer.getCustomerId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    /**
     * Delete a non existent customer
     * should fail with a 404 status code
     * @throws Exception if the test fails which it should
     */
    @Test
    public void testDeleteCustomer_Failure() throws Exception {
        mockMvc.perform(delete("/customer/200000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Get a customer by their id successfully
     * @throws Exception if the test fails which it shouldn't
     */
    @Test
    public void getCustomerById_Success() throws Exception {
        savedCustomer = customerService.create(new CustomerDAO(names.get(0), names.get(1)));
        mockMvc.perform(get("/customer/" + savedCustomer.getCustomerId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(savedCustomer.getCustomerId()))
                .andExpect(jsonPath("$.firstName").value(names.get(0)))
                .andExpect(jsonPath("$.lastName").value(names.get(1)));
    }

    /**
     * Get a customer by an id that doesn't exist
     * should fail with a 404 status code
     * @throws Exception if the test fails which it should
     */
    @Test
    public void getCustomerById_Failure() throws Exception {
        mockMvc.perform(get("/customer/200000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Update a customer successfully
     * @throws Exception if the test fails which it shouldn't
     */
    @Test
    public void updateCustomer_Success() throws Exception {
        savedCustomer = customerService.create(new CustomerDAO(names.get(0), names.get(1)));

        List<String> newNames = Arrays.asList(names.get(0) + "e", names.get(1) + "y");

        CreateCustomerDTO customerDTO = new CreateCustomerDTO(newNames.get(0), newNames.get(1));

        mockMvc.perform(put("/customer/" + savedCustomer.getCustomerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(savedCustomer.getCustomerId()))
                .andExpect(jsonPath("$.firstName").value(newNames.get(0)))
                .andExpect(jsonPath("$.lastName").value(newNames.get(1)));
    }

    /**
     * Update a customer that doesn't exist
     * should fail with a 404 status code
     * @throws Exception if the test fails which it should
     */
    @Test
    public void updateCustomerNotFound_Failure() throws Exception {
        CreateCustomerDTO customerDTO = new CreateCustomerDTO(names.get(0), names.get(1));
        mockMvc.perform(put("/customer/200000000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isNotFound());
    }

    /**
     * Update a customer with invalid names
     * @throws Exception if the test fails which it should
     */
    @Test
    public void testUpdateCustomer_Failure() throws Exception {
        savedCustomer = customerService.create(new CustomerDAO(names.get(0), names.get(1)));
        CreateCustomerDTO customerDTO = new CreateCustomerDTO("L", "jdfkfkflflkfdlkfldkfldkldflkfdlk}~~~@");
        mockMvc.perform(put("/customer/" + savedCustomer.getCustomerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cause").value("Names must be more than 2 characters, less than 30 characters and alphabetical"));
    }
}
