package com.wms.supplyservice.dto.request;

import com.wms.supplyservice.entity.SupplierStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSupplierStatusRequest {

    @NotNull(message = "Status is required")
    private SupplierStatus status;
}
