package com.example.spacecatsmarket.web.mapper;

import com.example.spacecatsmarket.common.CommunicationChannel;
import com.example.spacecatsmarket.domain.customer.CustomerDetails;
import com.example.spacecatsmarket.dto.customer.CustomerDetailsDto;
import com.example.spacecatsmarket.dto.customer.CustomerDetailsEntry;
import com.example.spacecatsmarket.dto.customer.CustomerDetailsListDto;
import com.example.spacecatsmarket.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomDetailsMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "preferredChannel", expression = "java(preferredChannelToStrings(customerDetails.getPreferredChannel()))")
    CustomerDetailsDto toCustomerDetailsDto(CustomerDetails customerDetails);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "preferredChannel", expression = "java(stringsToPreferredChannel(customerEntity.getPreferredChannel()))")
    CustomerDetails toCustomerDetails(CustomerEntity customerEntity);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "preferredChannel", expression = "java(preferredChannelToStrings(customerDetails.getPreferredChannel()))")
    CustomerEntity toCustomerEntity(CustomerDetails customerDetails);

    default CustomerDetailsListDto toCustomerDetailsListDto(List<CustomerDetails> customerDetails) {
        return CustomerDetailsListDto.builder()
                .customerDetailsEntries(toCustomerDetailsEntry(customerDetails))
                .build();
    }

    List<CustomerDetailsEntry> toCustomerDetailsEntry(List<CustomerDetails> customerDetails);

    default List<String> preferredChannelToStrings(List<CommunicationChannel> preferredChannel) {
        if (preferredChannel == null) return null;
        return preferredChannel.stream()
                .map(CommunicationChannel::name)
                .collect(Collectors.toList());
    }

    default List<CommunicationChannel> stringsToPreferredChannel(List<String> preferredChannel) {
        if (preferredChannel == null) return null;
        return preferredChannel.stream()
                .map(CommunicationChannel::valueOf)
                .collect(Collectors.toList());
    }
}
