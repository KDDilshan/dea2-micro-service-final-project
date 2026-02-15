package com.wms.supplyservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiveUpdateRequest {

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<ReceiveUpdateItemRequest> items;
}
