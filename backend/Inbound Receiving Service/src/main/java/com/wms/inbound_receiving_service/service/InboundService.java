package com.wms.inbound_receiving_service.service;

import com.wms.inbound_receiving_service.dto.InboundRequestDTO;
import com.wms.inbound_receiving_service.dto.InboundResponseDTO;

public interface InboundService {

    InboundResponseDTO receiveShipment(InboundRequestDTO request);

    InboundResponseDTO getShipmentById(Long id);
}
