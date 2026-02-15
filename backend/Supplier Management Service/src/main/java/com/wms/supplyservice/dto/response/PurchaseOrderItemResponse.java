package com.wms.supplyservice.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderItemResponse {

    private UUID id;
    private String itemId;
    private int orderedQty;
    private int receivedQty;
    private BigDecimal unitCost;
}
