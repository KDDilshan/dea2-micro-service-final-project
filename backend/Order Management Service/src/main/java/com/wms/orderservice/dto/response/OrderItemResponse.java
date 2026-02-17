package com.wms.orderservice.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItemResponse(
        UUID id,
        String itemId,
        int requestedQty,
        int approvedQty,
        BigDecimal unitPrice
) {}
