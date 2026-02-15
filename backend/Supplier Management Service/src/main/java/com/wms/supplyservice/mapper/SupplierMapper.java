package com.wms.supplyservice.mapper;

import com.wms.supplyservice.dto.request.CreateSupplierRequest;
import com.wms.supplyservice.dto.response.SupplierResponse;
import com.wms.supplyservice.entity.Supplier;
import com.wms.supplyservice.entity.SupplierStatus;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public Supplier toEntity(CreateSupplierRequest request, String supplierCode) {
        return Supplier.builder()
                .supplierCode(supplierCode)
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .status(SupplierStatus.ACTIVE)
                .build();
    }

    public SupplierResponse toResponse(Supplier entity) {
        return SupplierResponse.builder()
                .id(entity.getId())
                .supplierCode(entity.getSupplierCode())
                .name(entity.getName())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
