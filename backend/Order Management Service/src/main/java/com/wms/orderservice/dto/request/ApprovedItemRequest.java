package com.wms.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ApprovedItemRequest(
        @NotBlank(message = "Item ID is required")
        String itemId,

        @Min(value = 0, message = "Approved quantity cannot be negative")
        int approvedQty
) {}
