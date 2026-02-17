package com.wms.orderservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateOrderRequest(
        @NotBlank(message = "Customer ID is required")
        String customerId,

        @NotNull(message = "partialAllowed flag is required")
        Boolean partialAllowed,

        @NotEmpty(message = "Order must contain at least one item")
        @Valid
        List<CreateOrderItemRequest> items
) {}
