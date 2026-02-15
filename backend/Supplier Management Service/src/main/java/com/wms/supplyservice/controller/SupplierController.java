package com.wms.supplyservice.controller;

import com.wms.supplyservice.dto.request.CreateSupplierRequest;
import com.wms.supplyservice.dto.request.UpdateSupplierRequest;
import com.wms.supplyservice.dto.request.UpdateSupplierStatusRequest;
import com.wms.supplyservice.dto.response.SupplierListResponse;
import com.wms.supplyservice.dto.response.SupplierResponse;
import com.wms.supplyservice.entity.SupplierStatus;
import com.wms.supplyservice.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Tag(name = "Suppliers", description = "Supplier management endpoints")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    @Operation(summary = "Create a new supplier")
    public ResponseEntity<SupplierResponse> createSupplier(@Valid @RequestBody CreateSupplierRequest request) {
        SupplierResponse response = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable UUID id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @GetMapping
    @Operation(summary = "Get all suppliers with optional status filter")
    public ResponseEntity<SupplierListResponse> getAllSuppliers(
            @RequestParam(required = false) SupplierStatus status) {
        return ResponseEntity.ok(supplierService.getAllSuppliers(status));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update supplier details")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSupplierRequest request) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update supplier status (ACTIVE / INACTIVE)")
    public ResponseEntity<SupplierResponse> updateSupplierStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSupplierStatusRequest request) {
        return ResponseEntity.ok(supplierService.updateSupplierStatus(id, request));
    }
}
