package com.wms.inbound_receiving_service.client;

import com.wms.inbound_receiving_service.model.Supplier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "supplier-service", url = "http://13.61.10.56:8081")
public interface SupplierClient {
    // Used in receiveShipment()
    @GetMapping("/api/v1/suppliers/name/{name}")
    Supplier getSupplierByName(@PathVariable("name") String name);

    // Used in getAllReceipts() and getShipmentById()
    @GetMapping("/api/v1/suppliers/{id}")
    Supplier getSupplierById(@PathVariable("id") Long id);
}