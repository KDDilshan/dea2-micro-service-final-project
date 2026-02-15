package com.wms.supplyservice.repository;

import com.wms.supplyservice.entity.POStatus;
import com.wms.supplyservice.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {

    Optional<PurchaseOrder> findByPoNumber(String poNumber);

    List<PurchaseOrder> findByStatus(POStatus status);

    List<PurchaseOrder> findBySupplierId(UUID supplierId);

    List<PurchaseOrder> findByStatusAndSupplierId(POStatus status, UUID supplierId);

    Optional<PurchaseOrder> findTopByOrderByPoNumberDesc();
}
