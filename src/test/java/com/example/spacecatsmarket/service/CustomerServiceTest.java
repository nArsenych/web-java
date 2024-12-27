package com.example.spacecatsmarket.service;

import com.example.spacecatsmarket.AbstractIt;
import com.example.spacecatsmarket.domain.customer.CustomerDetails;
import com.example.spacecatsmarket.repository.CustomerRepository;
import com.example.spacecatsmarket.repository.entity.CustomerEntity;
import com.example.spacecatsmarket.service.exception.CustomerNotFoundException;
import com.example.spacecatsmarket.service.interfaces.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
@DisplayName("Customer Service Tests")
class CustomerServiceTest extends AbstractIt {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;


    private static UUID CUSTOMER_ID;

    @BeforeEach
    void setup() {
        customerRepository.deleteAll();

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName("Test Customer");
        customerEntity.setAddress("Test Address");
        customerEntity.setPhoneNumber("+123456789");
        customerEntity.setEmail("test@example.com");
        customerEntity.setPreferredChannel(List.of("EMAIL"));

        customerRepository.save(customerEntity);

        CUSTOMER_ID = customerEntity.getId();
    }

    @Test
    void shouldGetAllCustomerDetails() {
        List<CustomerDetails> customers = customerService.getAllCustomerDetails();

        assertNotNull(customers, "Customer list should not be null.");
        assertFalse(customers.isEmpty(), "Customer list should not be empty.");
    }

    @Test
    void shouldGetCustomerDetailsById() {
        CustomerDetails customer = customerService.getCustomerDetailsById(CUSTOMER_ID);

        assertNotNull(customer, "Customer should not be null.");
        assertEquals(CUSTOMER_ID, customer.getId(), "Customer ID should match the expected value.");
        assertEquals("Test Customer", customer.getName(), "Customer name should match the expected value.");
    }

    @Test
    void shouldThrowExceptionIfCustomerNotFound() {
        assertThrows(CustomerNotFoundException.class,
                () -> customerService.getCustomerDetailsById(UUID.randomUUID()),
                "Expected exception not thrown for non-existent customer ID.");
    }
}
