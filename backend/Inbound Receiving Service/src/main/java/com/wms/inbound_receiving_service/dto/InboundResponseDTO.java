package com.wms.inbound_receiving_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InboundResponseDTO {
    private Long id;
    private String supplierName;
    private String productName;
    private int quantity;
    private String status;
}
