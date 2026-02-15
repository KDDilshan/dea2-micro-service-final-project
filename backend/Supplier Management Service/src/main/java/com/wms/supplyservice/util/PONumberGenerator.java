package com.wms.supplyservice.util;

import com.wms.supplyservice.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
@RequiredArgsConstructor
public class PONumberGenerator {

    private final PurchaseOrderRepository purchaseOrderRepository;

    /**
     * Generates the next PO number in the format PO-YYYY-000001.
     * Reads the latest PO number from the DB and increments.
     */
    public String generateNextPONumber() {
        int currentYear = Year.now().getValue();
        String prefix = "PO-" + currentYear + "-";

        return purchaseOrderRepository.findTopByOrderByPoNumberDesc()
                .filter(po -> po.getPoNumber().startsWith(prefix))
                .map(po -> {
                    String lastNumber = po.getPoNumber();                       // e.g. PO-2026-000003
                    String[] parts = lastNumber.split("-");
                    int lastSeq = Integer.parseInt(parts[2]);                   // 3
                    return formatPONumber(currentYear, lastSeq + 1);
                })
                .orElse(formatPONumber(currentYear, 1));
    }

    private String formatPONumber(int year, int sequence) {
        return String.format("PO-%d-%06d", year, sequence);
    }
}
