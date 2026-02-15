package com.wms.supplyservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierListResponse {

    private List<SupplierResponse> suppliers;
    private int totalCount;
}
