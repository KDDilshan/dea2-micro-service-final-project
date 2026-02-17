package com.wms.orderservice.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record AvailabilityResponse(
        boolean canFulfill,
        List<MissingItemResponse> missingItems,
        List<SuggestedApprovedItem> suggestedApprovedItems
) {
    @Builder
    public record SuggestedApprovedItem(
            String itemId,
            int approvedQty
    ) {}
}
