package com.example.spacecatsmarket.dto.order;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class OrderAuditResponseDto {
    String id;
    String status;
}
