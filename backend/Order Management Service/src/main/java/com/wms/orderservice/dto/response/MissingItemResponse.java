package com.wms.orderservice.dto.response;

import lombok.Builder;

@Builder
public record MissingItemResponse(
        String itemId,
        int missingQty
) {}
