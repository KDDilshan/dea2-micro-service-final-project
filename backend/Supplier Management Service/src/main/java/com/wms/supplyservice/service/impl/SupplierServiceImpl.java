package com.wms.supplyservice.service.impl;

import com.wms.supplyservice.dto.request.CreateSupplierRequest;
import com.wms.supplyservice.dto.request.UpdateSupplierRequest;
import com.wms.supplyservice.dto.request.UpdateSupplierStatusRequest;
import com.wms.supplyservice.dto.response.SupplierListResponse;
import com.wms.supplyservice.dto.response.SupplierResponse;
import com.wms.supplyservice.entity.Supplier;
import com.wms.supplyservice.entity.SupplierStatus;
import com.wms.supplyservice.exception.NotFoundException;
import com.wms.supplyservice.mapper.SupplierMapper;
import com.wms.supplyservice.repository.SupplierRepository;
import com.wms.supplyservice.service.SupplierService;
import com.wms.supplyservice.util.SupplierCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final SupplierCodeGenerator supplierCodeGenerator;

    @Override
    @Transactional
    public SupplierResponse createSupplier(CreateSupplierRequest request) {
        String code = supplierCodeGenerator.generateNextCode();
        log.info("Creating supplier with code: {}", code);

        Supplier supplier = supplierMapper.toEntity(request, code);
        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + id));
        return supplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierListResponse getAllSuppliers(SupplierStatus status) {
        List<Supplier> suppliers;
        if (status != null) {
            suppliers = supplierRepository.findByStatus(status);
        } else {
            suppliers = supplierRepository.findAll();
        }

        List<SupplierResponse> responses = suppliers.stream()
                .map(supplierMapper::toResponse)
                .toList();

        return SupplierListResponse.builder()
                .suppliers(responses)
                .totalCount(responses.size())
                .build();
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(UUID id, UpdateSupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + id));

        if (request.getName() != null && !request.getName().isBlank()) {
            supplier.setName(request.getName());
        }
        if (request.getAddress() != null && !request.getAddress().isBlank()) {
            supplier.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            supplier.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            supplier.setEmail(request.getEmail());
        }

        Supplier updated = supplierRepository.save(supplier);
        log.info("Updated supplier: {}", updated.getSupplierCode());
        return supplierMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplierStatus(UUID id, UpdateSupplierStatusRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + id));

        supplier.setStatus(request.getStatus());
        Supplier updated = supplierRepository.save(supplier);
        log.info("Supplier {} status changed to {}", updated.getSupplierCode(), updated.getStatus());
        return supplierMapper.toResponse(updated);
    }
}
