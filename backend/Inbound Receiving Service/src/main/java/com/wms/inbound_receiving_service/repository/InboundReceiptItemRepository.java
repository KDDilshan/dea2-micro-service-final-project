package com.wms.inbound_receiving_service.repository;

import com.wms.inbound_receiving_service.model.InboundReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InboundReceiptItemRepository extends JpaRepository<InboundReceiptItem, Long> {
    List<InboundReceiptItem> findByReceipt_ReceiptId(Long receiptId);
    List<InboundReceiptItem> findByQualityStatus(String qualityStatus);
}