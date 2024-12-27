package com.example.spacecatsmarket.service.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {
    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer with id %s not found";

    public CustomerNotFoundException(UUID customerId) {
        super(String.format(CUSTOMER_NOT_FOUND_MESSAGE, customerId));
    }
}
