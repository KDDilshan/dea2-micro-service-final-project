package com.wms.supplyservice.dto.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class POValidationResponse {

    private boolean valid;
    private String poNumber;
    private String supplierId;
    private String supplierCode;
    private String supplierName;
    private String status;
    private String deliveryWarehouse;
    private OffsetDateTime expectedDeliveryDate;
    private List<RemainingItemResponse> remainingItems;
    private String message;
}
