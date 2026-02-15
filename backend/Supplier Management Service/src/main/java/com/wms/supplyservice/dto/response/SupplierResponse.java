package com.wms.supplyservice.dto.response;

import com.wms.supplyservice.entity.SupplierStatus;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponse {

    private UUID id;
    private String supplierCode;
    private String name;
    private String address;
    private String phone;
    private String email;
    private SupplierStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
