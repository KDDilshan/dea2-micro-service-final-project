package com.wms.supplyservice.service;

import com.wms.supplyservice.dto.request.CreateSupplierRequest;
import com.wms.supplyservice.dto.request.UpdateSupplierRequest;
import com.wms.supplyservice.dto.request.UpdateSupplierStatusRequest;
import com.wms.supplyservice.dto.response.SupplierListResponse;
import com.wms.supplyservice.dto.response.SupplierResponse;
import com.wms.supplyservice.entity.SupplierStatus;

import java.util.UUID;

public interface SupplierService {

    SupplierResponse createSupplier(CreateSupplierRequest request);

    SupplierResponse getSupplierById(UUID id);

    SupplierListResponse getAllSuppliers(SupplierStatus status);

    SupplierResponse updateSupplier(UUID id, UpdateSupplierRequest request);

    SupplierResponse updateSupplierStatus(UUID id, UpdateSupplierStatusRequest request);
}
