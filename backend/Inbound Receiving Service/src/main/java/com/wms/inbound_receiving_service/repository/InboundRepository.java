package com.wms.inbound_receiving_service.repository;

import com.wms.inbound_receiving_service.entity.InboundShipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundRepository extends JpaRepository<InboundShipment, Long> {
}
