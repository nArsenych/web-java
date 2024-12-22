package com.example.spacecatsmarket.service.impl;

import com.example.spacecatsmarket.domain.customer.CustomerDetails;
import com.example.spacecatsmarket.repository.CustomerRepository;
import com.example.spacecatsmarket.repository.entity.CustomerEntity;
import com.example.spacecatsmarket.service.exception.CustomerNotFoundException;
import com.example.spacecatsmarket.service.interfaces.CustomerService;
import com.example.spacecatsmarket.web.mapper.CustomDetailsMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomDetailsMapper customDetailsMapper;

    @Override
    public List<CustomerDetails> getAllCustomerDetails() {
        return customerRepository.findAll()
                .stream()
                .map(customDetailsMapper::toCustomerDetails)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDetails getCustomerDetailsById(Long customerId) {
        CustomerEntity customerEntity = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.info("Customer with id {} not found in database", customerId);
                    return new CustomerNotFoundException(customerId);
                });
        return customDetailsMapper.toCustomerDetails(customerEntity);
    }

    @Override
    public CustomerDetails createCustomer(CustomerDetails customerDetails) {
        CustomerEntity customerEntity = customDetailsMapper.toCustomerEntity(customerDetails);
        customerEntity = customerRepository.save(customerEntity);
        log.info("Customer with id {} created successfully", customerEntity.getId());
        return customDetailsMapper.toCustomerDetails(customerEntity);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            log.info("Attempted to delete customer with id {}, but it does not exist", customerId);
            throw new CustomerNotFoundException(customerId);
        }
        customerRepository.deleteById(customerId);
        log.info("Customer with id {} deleted successfully", customerId);
    }
}
