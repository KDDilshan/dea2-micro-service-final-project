package com.wms.workforce_equipment_service.controller;

import com.wms.workforce_equipment_service.dto.request.MaintenanceLogRequest;
import com.wms.workforce_equipment_service.dto.response.MaintenanceLogResponse;
import com.wms.workforce_equipment_service.service.IMaintenanceLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workforce-equipment/maintenance-logs")
@RequiredArgsConstructor
public class MaintenanceLogController {

    private final IMaintenanceLogService maintenanceLogService;

    @GetMapping
    public ResponseEntity<List<MaintenanceLogResponse>> getAllMaintenanceLogs() {
        return ResponseEntity.ok(maintenanceLogService.getAllMaintenanceLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceLogResponse> getMaintenanceLogById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceLogService.getMaintenanceLogById(id));
    }

    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<MaintenanceLogResponse>> getMaintenanceLogsByEquipmentId(@PathVariable Long equipmentId) {
        return ResponseEntity.ok(maintenanceLogService.getMaintenanceLogsByEquipmentId(equipmentId));
    }

    @PostMapping
    public ResponseEntity<MaintenanceLogResponse> createMaintenanceLog(@Valid @RequestBody MaintenanceLogRequest request) {
        MaintenanceLogResponse created = maintenanceLogService.createMaintenanceLog(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceLogResponse> updateMaintenanceLog(@PathVariable Long id,
                                                                        @Valid @RequestBody MaintenanceLogRequest request) {
        return ResponseEntity.ok(maintenanceLogService.updateMaintenanceLog(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceLog(@PathVariable Long id) {
        maintenanceLogService.deleteMaintenanceLog(id);
        return ResponseEntity.noContent().build();
    }
}
