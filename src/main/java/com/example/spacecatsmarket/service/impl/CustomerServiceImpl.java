package com.example.spacecatsmarket.service.impl;

import com.example.spacecatsmarket.common.CommunicationChannel;
import com.example.spacecatsmarket.domain.customer.CustomerDetails;
import com.example.spacecatsmarket.service.interfaces.CustomerService;
import com.example.spacecatsmarket.service.exception.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final List<CustomerDetails> customerDetails = buildAllCustomerDetailsMock();

    @Override
    public List<CustomerDetails> getAllCustomerDetails() {
        return customerDetails;
    }

    @Override
    public CustomerDetails getCustomerDetailsById(UUID customerId) {
        return Optional.of(customerDetails.stream()
                        .filter(details -> details.getId().equals(customerId)).findFirst())
                .get()
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    private List<CustomerDetails> buildAllCustomerDetailsMock() {
        return List.of(
                CustomerDetails.builder()
                        .id(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"))
                        .name("Alice Johnson")
                        .address("123 Cosmic Lane, Catnip City")
                        .phoneNumber("123-456-7890")
                        .email("alice@example.com")
                        .preferredChannel(List.of(CommunicationChannel.EMAIL, CommunicationChannel.MOBILE))
                        .build(),
                CustomerDetails.builder()
                        .id(UUID.fromString("a6e8c4f8-3f98-4427-b8ac-27d3b9d937d5"))
                        .name("Bob Smith")
                        .address("456 Galactic Blvd, Star Town")
                        .phoneNumber("987-654-3210")
                        .email("bob@example.com")
                        .preferredChannel(List.of(CommunicationChannel.MOBILE))
                        .build(),
                CustomerDetails.builder()
                        .id(UUID.fromString("2a3a70f5-5554-4339-8d6c-78f8c3d5d847"))
                        .name("Charlie Brown")
                        .address("789 Nebula Road, Space Village")
                        .phoneNumber("555-123-4567")
                        .email("charlie@example.com")
                        .preferredChannel(List.of(CommunicationChannel.EMAIL))
                        .build());
    }
}
