package com.example.spacecatsmarket.domain.customer;

import com.example.spacecatsmarket.common.CommunicationChannel;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class CustomerDetails {

    UUID id;
    String name;
    String address;
    String phoneNumber;
    String email;
    List<CommunicationChannel> preferredChannel;
}
