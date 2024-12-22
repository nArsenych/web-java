package com.example.spacecatsmarket.service;

import com.example.spacecatsmarket.domain.customer.CustomerDetails;
import com.example.spacecatsmarket.service.exception.CustomerNotFoundException;
import com.example.spacecatsmarket.service.interfaces.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
@DisplayName("Customer Service Tests")
class CustomerServiceTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.liquibase.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.liquibase.user", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.liquibase.password", POSTGRESQL_CONTAINER::getPassword);
    }

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void setup() {
        customerService.getAllCustomerDetails().forEach(customer -> customerService.deleteCustomer(customer.getId()));

        CustomerDetails testCustomer = CustomerDetails.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phoneNumber("+123456789")
                .address("Test Address")
                .build();
        customerService.createCustomer(testCustomer);
    }

    @Test
    void shouldGetAllCustomerDetails() {
        List<CustomerDetails> customers = customerService.getAllCustomerDetails();

        assertNotNull(customers, "Customer list should not be null.");
        assertFalse(customers.isEmpty(), "Customer list should not be empty.");
    }

    @Test
    void shouldGetCustomerDetailsById() {
        CustomerDetails customer = customerService.getCustomerDetailsById(1L);

        assertNotNull(customer, "Customer should not be null.");
        assertEquals(1L, customer.getId(), "Customer ID should match the expected value.");
        assertEquals("Test Customer", customer.getName(), "Customer name should match the expected value.");
    }

    @Test
    void shouldThrowExceptionIfCustomerNotFound() {
        assertThrows(CustomerNotFoundException.class,
                () -> customerService.getCustomerDetailsById(99L),
                "Expected exception not thrown for non-existent customer ID.");
    }
}
