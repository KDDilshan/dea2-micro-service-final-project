package com.wms.inbound_receiving_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundReceiptDTO {
    private Long id;
    private String receiptNumber;
    private String supplierName;
    private LocalDateTime receivedAt;
    private String status;
}