package com.wms.supplyservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiveUpdateItemRequest {

    @NotBlank(message = "Item ID is required")
    private String itemId;

    @Min(value = 1, message = "Received quantity must be at least 1")
    private int receivedQtyNow;
}
