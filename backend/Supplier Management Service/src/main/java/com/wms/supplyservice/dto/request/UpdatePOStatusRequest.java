package com.wms.supplyservice.dto.request;

import com.wms.supplyservice.entity.POStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePOStatusRequest {

    @NotNull(message = "Status is required")
    private POStatus status;
}
