package com.wms.supplyservice.service;

import com.wms.supplyservice.dto.request.CreatePurchaseOrderRequest;
import com.wms.supplyservice.dto.request.ReceiveUpdateRequest;
import com.wms.supplyservice.dto.request.UpdatePOStatusRequest;
import com.wms.supplyservice.dto.response.POValidationResponse;
import com.wms.supplyservice.dto.response.PurchaseOrderListResponse;
import com.wms.supplyservice.dto.response.PurchaseOrderResponse;
import com.wms.supplyservice.entity.POStatus;

import java.util.UUID;

public interface PurchaseOrderService {

    PurchaseOrderResponse createPurchaseOrder(CreatePurchaseOrderRequest request);

    PurchaseOrderResponse getPurchaseOrderById(UUID id);

    PurchaseOrderListResponse getAllPurchaseOrders(POStatus status, UUID supplierId);

    PurchaseOrderResponse submitPurchaseOrder(UUID id);

    PurchaseOrderResponse approvePurchaseOrder(UUID id);

    PurchaseOrderResponse sendPurchaseOrder(UUID id);

    PurchaseOrderResponse cancelPurchaseOrder(UUID id);

    PurchaseOrderResponse updatePOStatus(UUID id, UpdatePOStatusRequest request);

    POValidationResponse validatePurchaseOrder(String poNumber);

    PurchaseOrderResponse receiveUpdate(String poNumber, ReceiveUpdateRequest request);
}
