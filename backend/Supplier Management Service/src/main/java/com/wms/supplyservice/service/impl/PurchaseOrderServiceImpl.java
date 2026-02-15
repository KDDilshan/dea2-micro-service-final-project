package com.wms.supplyservice.service.impl;

import com.wms.supplyservice.dto.request.CreatePurchaseOrderRequest;
import com.wms.supplyservice.dto.request.ReceiveUpdateItemRequest;
import com.wms.supplyservice.dto.request.ReceiveUpdateRequest;
import com.wms.supplyservice.dto.request.UpdatePOStatusRequest;
import com.wms.supplyservice.dto.response.POValidationResponse;
import com.wms.supplyservice.dto.response.PurchaseOrderListResponse;
import com.wms.supplyservice.dto.response.PurchaseOrderResponse;
import com.wms.supplyservice.dto.response.RemainingItemResponse;
import com.wms.supplyservice.entity.*;
import com.wms.supplyservice.exception.BusinessException;
import com.wms.supplyservice.exception.NotFoundException;
import com.wms.supplyservice.mapper.PurchaseOrderMapper;
import com.wms.supplyservice.repository.PurchaseOrderRepository;
import com.wms.supplyservice.repository.SupplierRepository;
import com.wms.supplyservice.service.PurchaseOrderService;
import com.wms.supplyservice.util.PONumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PONumberGenerator poNumberGenerator;

    /* ── Allowed transitions map ── */
    private static final Map<POStatus, Set<POStatus>> ALLOWED_TRANSITIONS = Map.of(
            POStatus.DRAFT, Set.of(POStatus.SUBMITTED, POStatus.CANCELLED),
            POStatus.SUBMITTED, Set.of(POStatus.APPROVED, POStatus.CANCELLED),
            POStatus.APPROVED, Set.of(POStatus.SENT, POStatus.CANCELLED),
            POStatus.SENT, Set.of(POStatus.PARTIALLY_RECEIVED, POStatus.RECEIVED, POStatus.CANCELLED),
            POStatus.PARTIALLY_RECEIVED, Set.of(POStatus.RECEIVED, POStatus.CANCELLED));

    /* ── Statuses that require an ACTIVE supplier ── */
    private static final Set<POStatus> REQUIRES_ACTIVE_SUPPLIER = Set.of(
            POStatus.SUBMITTED, POStatus.APPROVED, POStatus.SENT);

    // ────────────────────────────────────────────────────────────────────
    // CREATE
    // ────────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public PurchaseOrderResponse createPurchaseOrder(CreatePurchaseOrderRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + request.getSupplierId()));

        String poNumber = poNumberGenerator.generateNextPONumber();
        log.info("Creating purchase order: {}", poNumber);

        PurchaseOrder po = purchaseOrderMapper.toEntity(request, supplier, poNumber);
        PurchaseOrder saved = purchaseOrderRepository.save(po);
        return purchaseOrderMapper.toResponse(saved);
    }

    // ────────────────────────────────────────────────────────────────────
    // READ
    // ────────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getPurchaseOrderById(UUID id) {
        PurchaseOrder po = findPOById(id);
        return purchaseOrderMapper.toResponse(po);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderListResponse getAllPurchaseOrders(POStatus status, UUID supplierId) {
        List<PurchaseOrder> orders;

        if (status != null && supplierId != null) {
            orders = purchaseOrderRepository.findByStatusAndSupplierId(status, supplierId);
        } else if (status != null) {
            orders = purchaseOrderRepository.findByStatus(status);
        } else if (supplierId != null) {
            orders = purchaseOrderRepository.findBySupplierId(supplierId);
        } else {
            orders = purchaseOrderRepository.findAll();
        }

        List<PurchaseOrderResponse> responses = orders.stream()
                .map(purchaseOrderMapper::toResponse)
                .toList();

        return PurchaseOrderListResponse.builder()
                .purchaseOrders(responses)
                .totalCount(responses.size())
                .build();
    }

    // ────────────────────────────────────────────────────────────────────
    // STATUS TRANSITIONS (named endpoints)
    // ────────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public PurchaseOrderResponse submitPurchaseOrder(UUID id) {
        return transitionStatus(id, POStatus.SUBMITTED);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse approvePurchaseOrder(UUID id) {
        return transitionStatus(id, POStatus.APPROVED);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse sendPurchaseOrder(UUID id) {
        return transitionStatus(id, POStatus.SENT);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse cancelPurchaseOrder(UUID id) {
        PurchaseOrder po = findPOById(id);

        if (po.getStatus() == POStatus.RECEIVED) {
            throw new BusinessException("Cannot cancel a fully received purchase order");
        }
        if (po.getStatus() == POStatus.CANCELLED) {
            throw new BusinessException("Purchase order is already cancelled");
        }

        po.setStatus(POStatus.CANCELLED);
        PurchaseOrder saved = purchaseOrderRepository.save(po);
        log.info("PO {} cancelled", saved.getPoNumber());
        return purchaseOrderMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse updatePOStatus(UUID id, UpdatePOStatusRequest request) {
        return transitionStatus(id, request.getStatus());
    }

    // ────────────────────────────────────────────────────────────────────
    // INTEGRATION — VALIDATE
    // ────────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public POValidationResponse validatePurchaseOrder(String poNumber) {
        PurchaseOrder po = purchaseOrderRepository.findByPoNumber(poNumber)
                .orElseThrow(() -> new NotFoundException("Purchase order not found with PO number: " + poNumber));

        Set<POStatus> receivableStatuses = Set.of(POStatus.APPROVED, POStatus.SENT, POStatus.PARTIALLY_RECEIVED);

        if (!receivableStatuses.contains(po.getStatus())) {
            return POValidationResponse.builder()
                    .valid(false)
                    .poNumber(po.getPoNumber())
                    .status(po.getStatus().name())
                    .message("Purchase order is not in a receivable status. Current status: " + po.getStatus())
                    .build();
        }

        Supplier supplier = po.getSupplier();

        List<RemainingItemResponse> remainingItems = po.getItems().stream()
                .map(item -> RemainingItemResponse.builder()
                        .itemId(item.getItemId())
                        .remainingQty(item.getOrderedQty() - item.getReceivedQty())
                        .build())
                .toList();

        boolean allReceived = remainingItems.stream().allMatch(r -> r.getRemainingQty() == 0);

        if (allReceived) {
            return POValidationResponse.builder()
                    .valid(false)
                    .poNumber(po.getPoNumber())
                    .supplierId(supplier.getId().toString())
                    .supplierCode(supplier.getSupplierCode())
                    .supplierName(supplier.getName())
                    .status(po.getStatus().name())
                    .deliveryWarehouse(po.getDeliveryWarehouse())
                    .expectedDeliveryDate(po.getExpectedDeliveryDate())
                    .remainingItems(remainingItems)
                    .message("Already fully received")
                    .build();
        }

        return POValidationResponse.builder()
                .valid(true)
                .poNumber(po.getPoNumber())
                .supplierId(supplier.getId().toString())
                .supplierCode(supplier.getSupplierCode())
                .supplierName(supplier.getName())
                .status(po.getStatus().name())
                .deliveryWarehouse(po.getDeliveryWarehouse())
                .expectedDeliveryDate(po.getExpectedDeliveryDate())
                .remainingItems(remainingItems)
                .message("OK")
                .build();
    }

    // ────────────────────────────────────────────────────────────────────
    // INTEGRATION — RECEIVE UPDATE
    // ────────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public PurchaseOrderResponse receiveUpdate(String poNumber, ReceiveUpdateRequest request) {
        PurchaseOrder po = purchaseOrderRepository.findByPoNumber(poNumber)
                .orElseThrow(() -> new NotFoundException("Purchase order not found with PO number: " + poNumber));

        Set<POStatus> receivableStatuses = Set.of(POStatus.APPROVED, POStatus.SENT, POStatus.PARTIALLY_RECEIVED);
        if (!receivableStatuses.contains(po.getStatus())) {
            throw new BusinessException("Cannot receive goods for PO in status: " + po.getStatus());
        }

        // Build a lookup of items by itemId
        Map<String, PurchaseOrderItem> itemMap = po.getItems().stream()
                .collect(Collectors.toMap(PurchaseOrderItem::getItemId, item -> item));

        for (ReceiveUpdateItemRequest updateItem : request.getItems()) {
            PurchaseOrderItem poItem = itemMap.get(updateItem.getItemId());
            if (poItem == null) {
                throw new BusinessException("Item not found in PO: " + updateItem.getItemId());
            }

            int newReceived = poItem.getReceivedQty() + updateItem.getReceivedQtyNow();
            if (newReceived > poItem.getOrderedQty()) {
                throw new BusinessException(
                        String.format("Received quantity (%d) exceeds ordered quantity (%d) for item %s",
                                newReceived, poItem.getOrderedQty(), updateItem.getItemId()));
            }

            poItem.setReceivedQty(newReceived);
        }

        // Determine new PO status
        boolean allFullyReceived = po.getItems().stream()
                .allMatch(item -> item.getReceivedQty() == item.getOrderedQty());
        boolean anyPartiallyReceived = po.getItems().stream()
                .anyMatch(item -> item.getReceivedQty() > 0);

        if (allFullyReceived) {
            po.setStatus(POStatus.RECEIVED);
        } else if (anyPartiallyReceived) {
            po.setStatus(POStatus.PARTIALLY_RECEIVED);
        }

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        log.info("PO {} receive-update processed. New status: {}", saved.getPoNumber(), saved.getStatus());
        return purchaseOrderMapper.toResponse(saved);
    }

    // ────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ────────────────────────────────────────────────────────────────────

    private PurchaseOrder findPOById(UUID id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase order not found with id: " + id));
    }

    private PurchaseOrderResponse transitionStatus(UUID id, POStatus targetStatus) {
        PurchaseOrder po = findPOById(id);
        POStatus currentStatus = po.getStatus();

        // Check allowed transitions
        Set<POStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Set.of());
        if (!allowed.contains(targetStatus)) {
            throw new BusinessException(
                    String.format("Cannot transition from %s to %s", currentStatus, targetStatus));
        }

        // Check supplier is active for certain transitions
        if (REQUIRES_ACTIVE_SUPPLIER.contains(targetStatus)) {
            Supplier supplier = po.getSupplier();
            if (supplier.getStatus() == SupplierStatus.INACTIVE) {
                throw new BusinessException(
                        "Cannot " + targetStatus.name().toLowerCase() + " a PO for an inactive supplier");
            }
        }

        po.setStatus(targetStatus);
        PurchaseOrder saved = purchaseOrderRepository.save(po);
        log.info("PO {} transitioned from {} to {}", saved.getPoNumber(), currentStatus, targetStatus);
        return purchaseOrderMapper.toResponse(saved);
    }
}
