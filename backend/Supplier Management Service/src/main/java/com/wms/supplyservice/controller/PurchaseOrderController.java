package com.wms.supplyservice.controller;

import com.wms.supplyservice.dto.request.CreatePurchaseOrderRequest;
import com.wms.supplyservice.dto.request.ReceiveUpdateRequest;
import com.wms.supplyservice.dto.request.UpdatePOStatusRequest;
import com.wms.supplyservice.dto.response.POValidationResponse;
import com.wms.supplyservice.dto.response.PurchaseOrderListResponse;
import com.wms.supplyservice.dto.response.PurchaseOrderResponse;
import com.wms.supplyservice.entity.POStatus;
import com.wms.supplyservice.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
@Tag(name = "Purchase Orders", description = "Purchase order management and integration endpoints")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    // ────────────────────────────────────────────────────────────────────
    // CRUD
    // ────────────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new purchase order in DRAFT status")
    public ResponseEntity<PurchaseOrderResponse> createPurchaseOrder(
            @Valid @RequestBody CreatePurchaseOrderRequest request) {
        PurchaseOrderResponse response = purchaseOrderService.createPurchaseOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase order by ID")
    public ResponseEntity<PurchaseOrderResponse> getPurchaseOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrderById(id));
    }

    @GetMapping
    @Operation(summary = "Get all purchase orders with optional filters")
    public ResponseEntity<PurchaseOrderListResponse> getAllPurchaseOrders(
            @RequestParam(required = false) POStatus status,
            @RequestParam(required = false) UUID supplierId) {
        return ResponseEntity.ok(purchaseOrderService.getAllPurchaseOrders(status, supplierId));
    }

    // ────────────────────────────────────────────────────────────────────
    // LIFECYCLE TRANSITIONS
    // ────────────────────────────────────────────────────────────────────

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit purchase order (DRAFT → SUBMITTED)")
    public ResponseEntity<PurchaseOrderResponse> submitPurchaseOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.submitPurchaseOrder(id));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve purchase order (SUBMITTED → APPROVED)")
    public ResponseEntity<PurchaseOrderResponse> approvePurchaseOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.approvePurchaseOrder(id));
    }

    @PostMapping("/{id}/send")
    @Operation(summary = "Send purchase order to supplier (APPROVED → SENT)")
    public ResponseEntity<PurchaseOrderResponse> sendPurchaseOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.sendPurchaseOrder(id));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel purchase order (not allowed if RECEIVED)")
    public ResponseEntity<PurchaseOrderResponse> cancelPurchaseOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.cancelPurchaseOrder(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Generic status update (enforces lifecycle rules)")
    public ResponseEntity<PurchaseOrderResponse> updatePOStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePOStatusRequest request) {
        return ResponseEntity.ok(purchaseOrderService.updatePOStatus(id, request));
    }

    // ────────────────────────────────────────────────────────────────────
    // INTEGRATION ENDPOINTS (called by Inbound Receiving Service)
    // ────────────────────────────────────────────────────────────────────

    @GetMapping("/validate/{poNumber}")
    @Operation(summary = "Validate PO for receiving (integration endpoint)")
    public ResponseEntity<POValidationResponse> validatePurchaseOrder(@PathVariable String poNumber) {
        return ResponseEntity.ok(purchaseOrderService.validatePurchaseOrder(poNumber));
    }

    @PostMapping("/receive-update/{poNumber}")
    @Operation(summary = "Receive goods against a PO (integration endpoint)")
    public ResponseEntity<PurchaseOrderResponse> receiveUpdate(
            @PathVariable String poNumber,
            @Valid @RequestBody ReceiveUpdateRequest request) {
        return ResponseEntity.ok(purchaseOrderService.receiveUpdate(poNumber, request));
    }
}
