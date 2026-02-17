package com.wms.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CreateOrderItemRequest(
        @NotBlank(message = "Item ID is required")
        String itemId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        BigDecimal unitPrice
) {}
