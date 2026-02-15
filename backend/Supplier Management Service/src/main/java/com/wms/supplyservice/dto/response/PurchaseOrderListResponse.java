package com.wms.supplyservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderListResponse {

    private List<PurchaseOrderResponse> purchaseOrders;
    private int totalCount;
}
