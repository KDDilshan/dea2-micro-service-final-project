package com.wms.workforce_equipment_service.controller;

import com.wms.workforce_equipment_service.dto.request.EquipmentRequest;
import com.wms.workforce_equipment_service.dto.response.EquipmentResponse;
import com.wms.workforce_equipment_service.service.IEquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workforce-equipment/equipments")
@RequiredArgsConstructor
public class WorkforceEquipmentController {

    private final IEquipmentService equipmentService;

    @GetMapping("/hello")
    public String sayHi() {
        return "Hi";
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponse>> getAllEquipments() {
        return ResponseEntity.ok(equipmentService.getAllEquipments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @PostMapping
    public ResponseEntity<EquipmentResponse> createEquipment(@Valid @RequestBody EquipmentRequest request) {
        EquipmentResponse created = equipmentService.createEquipment(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponse> updateEquipment(@PathVariable Long id,
                                                             @Valid @RequestBody EquipmentRequest request) {
        return ResponseEntity.ok(equipmentService.updateEquipment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
}
