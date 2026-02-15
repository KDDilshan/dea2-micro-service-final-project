package com.wms.supplyservice.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSupplierRequest {

    private String name;

    private String address;

    private String phone;

    @Email(message = "Email must be valid")
    private String email;
}
