package com.wms.inbound_receiving_service.dto;

import lombok.Data;

@Data
public class InboundRequestDTO {
    private String supplierName;
    private String productName;
    private int quantity;
}
