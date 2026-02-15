package com.wms.supplyservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePurchaseOrderItemRequest {

    @NotBlank(message = "Item ID is required")
    private String itemId;

    @Min(value = 1, message = "Ordered quantity must be at least 1")
    private int orderedQty;

    private BigDecimal unitCost;
}
