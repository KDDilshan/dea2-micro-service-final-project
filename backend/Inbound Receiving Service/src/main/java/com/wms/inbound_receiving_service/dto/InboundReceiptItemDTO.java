package com.wms.inbound_receiving_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundReceiptItemDTO {
    private Long id;
    private Long receiptId;
    private String productName;
    private int quantityReceived;
    private String condition;
}