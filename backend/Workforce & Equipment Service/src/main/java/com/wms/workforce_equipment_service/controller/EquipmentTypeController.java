package com.wms.workforce_equipment_service.controller;

import com.wms.workforce_equipment_service.dto.request.EquipmentTypeRequest;
import com.wms.workforce_equipment_service.dto.response.EquipmentTypeResponse;
import com.wms.workforce_equipment_service.service.IEquipmentTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment-types")
@RequiredArgsConstructor
public class EquipmentTypeController {

    private final IEquipmentTypeService equipmentTypeService;

    @GetMapping
    public ResponseEntity<List<EquipmentTypeResponse>> getAllEquipmentTypes() {
        return ResponseEntity.ok(equipmentTypeService.getAllEquipmentTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentTypeResponse> getEquipmentTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentTypeService.getEquipmentTypeById(id));
    }

    @PostMapping
    public ResponseEntity<EquipmentTypeResponse> createEquipmentType(@Valid @RequestBody EquipmentTypeRequest request) {
        EquipmentTypeResponse created = equipmentTypeService.createEquipmentType(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentTypeResponse> updateEquipmentType(@PathVariable Long id,
                                                                      @Valid @RequestBody EquipmentTypeRequest request) {
        return ResponseEntity.ok(equipmentTypeService.updateEquipmentType(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipmentType(@PathVariable Long id) {
        equipmentTypeService.deleteEquipmentType(id);
        return ResponseEntity.noContent().build();
    }
}
