package com.example.spacecatsmarket.service.interfaces;

import com.example.spacecatsmarket.domain.customer.CustomerDetails;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDetails> getAllCustomerDetails();

    CustomerDetails getCustomerDetailsById(UUID customerId);
}
