package com.example.spacecatsmarket.service;

import com.example.spacecatsmarket.domain.customer.CustomerDetails;
import com.example.spacecatsmarket.service.exception.CustomerNotFoundException;
import com.example.spacecatsmarket.service.impl.CustomerServiceImpl;
import com.example.spacecatsmarket.service.interfaces.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CustomerServiceImpl.class)
@DisplayName("Customer Service Tests")
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    private static final UUID CUSTOMER_ID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

    @Test
    void shouldGetAllCustomerDetails() {
        List<CustomerDetails> customers = customerService.getAllCustomerDetails();

        assertFalse(customers.isEmpty());
    }

    @Test
    void shouldGetCustomerDetailsById() {
        CustomerDetails customer = customerService.getCustomerDetailsById(CUSTOMER_ID);

        assertNotNull(customer);
        assertEquals(CUSTOMER_ID, customer.getId());
    }

    @Test
    void shouldThrowExceptionIfCustomerNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerDetailsById(nonExistentId));
    }
}
