package com.wms.inbound_receiving_service.repository;

import com.wms.inbound_receiving_service.model.InboundReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboundReceiptRepository extends JpaRepository<InboundReceipt, Long> {

    List<InboundReceipt> findAllByOrderByReceiptDateDesc();

    Optional<InboundReceipt> findByGrnNumber(String grnNumber);

    List<InboundReceipt> findByStatus(String status);
}