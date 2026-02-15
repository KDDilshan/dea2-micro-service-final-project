package com.wms.supplyservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePurchaseOrderRequest {

    @NotNull(message = "Supplier ID is required")
    private UUID supplierId;

    @NotNull(message = "Expected delivery date is required")
    private OffsetDateTime expectedDeliveryDate;

    @NotBlank(message = "Delivery warehouse is required")
    private String deliveryWarehouse;

    private String notes;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<CreatePurchaseOrderItemRequest> items;
}
