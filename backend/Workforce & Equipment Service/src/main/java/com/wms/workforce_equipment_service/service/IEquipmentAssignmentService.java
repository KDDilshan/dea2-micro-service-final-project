package com.wms.workforce_equipment_service.service;

import com.wms.workforce_equipment_service.dto.request.EquipmentAssignmentRequest;
import com.wms.workforce_equipment_service.dto.response.EquipmentAssignmentResponse;

import java.util.List;

public interface IEquipmentAssignmentService {

    List<EquipmentAssignmentResponse> getAllAssignments();

    EquipmentAssignmentResponse getAssignmentById(Long id);

    List<EquipmentAssignmentResponse> getAssignmentsByEquipmentId(Long equipmentId);

    List<EquipmentAssignmentResponse> getAssignmentsByWorkerId(Long workerId);

    EquipmentAssignmentResponse createAssignment(EquipmentAssignmentRequest request);

    EquipmentAssignmentResponse updateAssignment(Long id, EquipmentAssignmentRequest request);

    void deleteAssignment(Long id);
}
