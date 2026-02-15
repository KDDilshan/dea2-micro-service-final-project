package com.wms.supplyservice.mapper;

import com.wms.supplyservice.dto.request.CreatePurchaseOrderItemRequest;
import com.wms.supplyservice.dto.request.CreatePurchaseOrderRequest;
import com.wms.supplyservice.dto.response.PurchaseOrderItemResponse;
import com.wms.supplyservice.dto.response.PurchaseOrderResponse;
import com.wms.supplyservice.entity.PurchaseOrder;
import com.wms.supplyservice.entity.PurchaseOrderItem;
import com.wms.supplyservice.entity.Supplier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PurchaseOrderMapper {

    public PurchaseOrder toEntity(CreatePurchaseOrderRequest request, Supplier supplier, String poNumber) {
        PurchaseOrder po = PurchaseOrder.builder()
                .poNumber(poNumber)
                .supplier(supplier)
                .expectedDeliveryDate(request.getExpectedDeliveryDate())
                .deliveryWarehouse(request.getDeliveryWarehouse())
                .notes(request.getNotes())
                .build();

        if (request.getItems() != null) {
            for (CreatePurchaseOrderItemRequest itemReq : request.getItems()) {
                PurchaseOrderItem item = toItemEntity(itemReq);
                po.addItem(item);
            }
        }

        return po;
    }

    public PurchaseOrderItem toItemEntity(CreatePurchaseOrderItemRequest request) {
        return PurchaseOrderItem.builder()
                .itemId(request.getItemId())
                .orderedQty(request.getOrderedQty())
                .receivedQty(0)
                .unitCost(request.getUnitCost())
                .build();
    }

    public PurchaseOrderResponse toResponse(PurchaseOrder entity) {
        Supplier supplier = entity.getSupplier();

        List<PurchaseOrderItemResponse> itemResponses = entity.getItems().stream()
                .map(this::toItemResponse)
                .toList();

        return PurchaseOrderResponse.builder()
                .id(entity.getId())
                .poNumber(entity.getPoNumber())
                .supplierId(supplier.getId())
                .supplierCode(supplier.getSupplierCode())
                .supplierName(supplier.getName())
                .status(entity.getStatus())
                .expectedDeliveryDate(entity.getExpectedDeliveryDate())
                .deliveryWarehouse(entity.getDeliveryWarehouse())
                .notes(entity.getNotes())
                .items(itemResponses)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public PurchaseOrderItemResponse toItemResponse(PurchaseOrderItem item) {
        return PurchaseOrderItemResponse.builder()
                .id(item.getId())
                .itemId(item.getItemId())
                .orderedQty(item.getOrderedQty())
                .receivedQty(item.getReceivedQty())
                .unitCost(item.getUnitCost())
                .build();
    }
}
