package com.wms.orderservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record ApproveOrderRequest(
        @NotNull(message = "Approval type is required")
        ApprovalType approvalType,

        @Valid
        List<ApprovedItemRequest> approvedItems
) {
    public enum ApprovalType {
        FULL, PARTIAL, AUTO
    }
}
