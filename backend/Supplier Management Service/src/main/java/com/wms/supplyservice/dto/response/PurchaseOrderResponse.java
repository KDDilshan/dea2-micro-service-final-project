package com.wms.supplyservice.dto.response;

import com.wms.supplyservice.entity.POStatus;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderResponse {

    private UUID id;
    private String poNumber;
    private UUID supplierId;
    private String supplierCode;
    private String supplierName;
    private POStatus status;
    private OffsetDateTime expectedDeliveryDate;
    private String deliveryWarehouse;
    private String notes;
    private List<PurchaseOrderItemResponse> items;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
