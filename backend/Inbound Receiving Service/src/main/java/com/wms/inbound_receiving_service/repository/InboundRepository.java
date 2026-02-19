package com.wms.inbound_receiving_service.repository;

import com.wms.inbound_receiving_service.model.InboundShipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InboundRepository extends JpaRepository<InboundShipment, Long> {

    List<InboundShipment> findByStatus(String status);

    List<InboundShipment> findBySupplierNameContainingIgnoreCase(String supplierName);
}