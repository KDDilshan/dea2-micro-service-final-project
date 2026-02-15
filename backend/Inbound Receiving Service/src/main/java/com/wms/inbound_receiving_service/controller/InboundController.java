package com.wms.inbound_receiving_service.controller;

import com.wms.inbound_receiving_service.dto.InboundRequestDTO;
import com.wms.inbound_receiving_service.dto.InboundResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inbound")
@RequiredArgsConstructor
public class InboundController {

    private final InboundService inboundService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InboundResponseDTO receive(@RequestBody InboundRequestDTO request) {
        return inboundService.receiveShipment(request);
    }

    @GetMapping("/{id}")
    public InboundResponseDTO getById(@PathVariable Long id) {
        return inboundService.getShipmentById(id);
    }
}
