package com.wms.inbound_receiving_service.service.impl;

import com.wms.inbound_receiving_service.dto.InboundRequestDTO;
import com.wms.inbound_receiving_service.dto.InboundResponseDTO;
import com.wms.inbound_receiving_service.entity.InboundShipment;
import com.wms.inbound_receiving_service.repository.InboundRepository;
import com.wms.inbound_receiving_service.service.InboundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InboundServiceImpl implements InboundService {

    private final InboundRepository inboundRepository;

    @Override
    public InboundResponseDTO receiveShipment(InboundRequestDTO request) {
        InboundShipment shipment = new InboundShipment();
        shipment.setSupplierName(request.getSupplierName());
        shipment.setProductName(request.getProductName());
        shipment.setQuantity(request.getQuantity());
        shipment.setStatus("RECEIVED");

        InboundShipment saved = inboundRepository.save(shipment);
        return map(saved);
    }

    @Override
    public InboundResponseDTO getShipmentById(Long id) {
        InboundShipment shipment = inboundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));
        return map(shipment);
    }

    private InboundResponseDTO map(InboundShipment shipment) {
        return InboundResponseDTO.builder()
                .id(shipment.getId())
                .supplierName(shipment.getSupplierName())
                .productName(shipment.getProductName())
                .quantity(shipment.getQuantity())
                .status(shipment.getStatus())
                .build();
    }
}
