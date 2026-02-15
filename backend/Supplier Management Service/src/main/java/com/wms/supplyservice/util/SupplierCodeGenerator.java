package com.wms.supplyservice.util;

import com.wms.supplyservice.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierCodeGenerator {

    private final SupplierRepository supplierRepository;

    /**
     * Generates the next supplier code in the format SUP-000001.
     * Reads the latest code from the DB and increments.
     */
    public String generateNextCode() {
        return supplierRepository.findTopByOrderBySupplierCodeDesc()
                .map(supplier -> {
                    String lastCode = supplier.getSupplierCode();           // e.g. SUP-000003
                    int lastNum = Integer.parseInt(lastCode.split("-")[1]); // 3
                    return formatCode(lastNum + 1);
                })
                .orElse(formatCode(1));
    }

    private String formatCode(int sequence) {
        return String.format("SUP-%06d", sequence);
    }
}
