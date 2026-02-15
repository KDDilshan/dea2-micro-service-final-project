package com.wms.supplyservice.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemainingItemResponse {

    private String itemId;
    private int remainingQty;
}
