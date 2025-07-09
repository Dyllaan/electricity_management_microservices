package com.louisfiges.provider.controllers;

import com.louisfiges.common.dtos.customer.CreateCustomerDTO;
import com.louisfiges.common.factories.ResponseEntityFactory;
import com.louisfiges.common.http.Response;
import com.louisfiges.provider.daos.CustomerDAO;
import com.louisfiges.provider.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Catch the data integrity error sometimes thrown if you submit a malformed request
     * @param createCustomerDTO an object representing the submission to create a customer
     * @return the customer saved as a dto or an error
     */
    @PostMapping
    public ResponseEntity<Response> createCustomer(@RequestBody CreateCustomerDTO createCustomerDTO) {
        try {

            if (customerService.isNameInvalid(createCustomerDTO.firstName()) || customerService.isNameInvalid(createCustomerDTO.lastName())) {
                return ResponseEntityFactory.create("Names must be more than 2 characters, less than 30 characters and alphabetical", HttpStatus.BAD_REQUEST);
            }

            CustomerDAO savedCustomer = customerService.create(new CustomerDAO(createCustomerDTO.firstName(), createCustomerDTO.lastName()));
            return ResponseEntityFactory.create(savedCustomer.toDTO(), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntityFactory.create("Problem with your request", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("An error occurred", e);
            return ResponseEntityFactory.create("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Response> getCustomerById(@PathVariable Long customerId) {
        try {
            CustomerDAO customerDAO = customerService.read(customerId);
            return ResponseEntityFactory.create(customerDAO.toDTO(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntityFactory.create(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete a customer by their id
     * returns blank if successful and 404 if not found
     * @param customerId the id of the customer to delete
     * @return a response entity with nothing but a status code (204 or 404)
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Response> deleteCustomerById(@PathVariable Long customerId) {
        try {
            if (!customerService.exists(customerId)) {
                return ResponseEntityFactory.create(HttpStatus.NOT_FOUND);
            }
            customerService.delete(customerId);
            return ResponseEntityFactory.create(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntityFactory.create(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * update the customers details, optionally
     * flag is for if none is passed
     * @param customerId the id of the customer to update
     * @param createCustomerDTO the new details
     * @return the updated customer or a 404 if not found or 500 if an error occurred
     */
    @PutMapping("/{customerId}")
    public ResponseEntity<Response> updateCustomer(@PathVariable Long customerId, @RequestBody CreateCustomerDTO createCustomerDTO) {
        try {
            Optional<CustomerDAO> customerOpt = customerService.find(customerId);
            if (customerOpt.isEmpty()) {
                return ResponseEntityFactory.create(HttpStatus.NOT_FOUND);
            }

            CustomerDAO customerDAO = customerOpt.get();

            boolean flag = false;

            if(createCustomerDTO.firstName() != null) {
                if (customerService.isNameInvalid(createCustomerDTO.firstName())) {
                    return ResponseEntityFactory.create("Names must be more than 2 characters, less than 30 characters and alphabetical", HttpStatus.BAD_REQUEST);
                }
                flag = true;
                customerDAO.setFirstName(createCustomerDTO.firstName());
            }

            if(createCustomerDTO.lastName() != null) {
                if (customerService.isNameInvalid(createCustomerDTO.lastName())) {
                    return ResponseEntityFactory.create("Names must be more than 2 characters, less than 30 characters and alphabetical", HttpStatus.BAD_REQUEST);
                }
                flag = true;
                customerDAO.setLastName(createCustomerDTO.lastName());
            }

            if(!flag) {
                return ResponseEntityFactory.create("No changes", HttpStatus.BAD_REQUEST);
            }

            customerService.create(customerDAO);

            return ResponseEntityFactory.create(customerDAO.toDTO(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An error occurred", e);
            return ResponseEntityFactory.create("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
