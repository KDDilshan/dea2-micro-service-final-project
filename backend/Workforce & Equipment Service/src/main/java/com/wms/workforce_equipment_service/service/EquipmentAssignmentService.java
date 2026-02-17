package com.wms.workforce_equipment_service.service;

import com.wms.workforce_equipment_service.dto.request.EquipmentAssignmentRequest;
import com.wms.workforce_equipment_service.dto.response.EquipmentAssignmentResponse;
import com.wms.workforce_equipment_service.exception.ResourceNotFoundException;
import com.wms.workforce_equipment_service.model.Equipment;
import com.wms.workforce_equipment_service.model.EquipmentAssignment;
import com.wms.workforce_equipment_service.repository.EquipmentAssignmentRepository;
import com.wms.workforce_equipment_service.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentAssignmentService implements IEquipmentAssignmentService {

    private final EquipmentAssignmentRepository assignmentRepository;
    private final EquipmentRepository equipmentRepository;

    @Override
    public List<EquipmentAssignmentResponse> getAllAssignments() {
        return assignmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EquipmentAssignmentResponse getAssignmentById(Long id) {
        EquipmentAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        return mapToResponse(assignment);
    }

    @Override
    public List<EquipmentAssignmentResponse> getAssignmentsByEquipmentId(Long equipmentId) {
        return assignmentRepository.findByEquipmentId(equipmentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipmentAssignmentResponse> getAssignmentsByWorkerId(Long workerId) {
        return assignmentRepository.findByWorkerId(workerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EquipmentAssignmentResponse createAssignment(EquipmentAssignmentRequest request) {
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + request.getEquipmentId()));

        EquipmentAssignment assignment = new EquipmentAssignment();
        assignment.setEquipment(equipment);
        assignment.setWorkerId(request.getWorkerId());
        assignment.setAssignedDate(request.getAssignedDate());
        assignment.setReturnedDate(request.getReturnedDate());
        assignment.setStatus(request.getStatus());

        EquipmentAssignment saved = assignmentRepository.save(assignment);
        return mapToResponse(saved);
    }

    @Override
    public EquipmentAssignmentResponse updateAssignment(Long id, EquipmentAssignmentRequest request) {
        EquipmentAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));

        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + request.getEquipmentId()));

        assignment.setEquipment(equipment);
        assignment.setWorkerId(request.getWorkerId());
        assignment.setAssignedDate(request.getAssignedDate());
        assignment.setReturnedDate(request.getReturnedDate());
        assignment.setStatus(request.getStatus());

        EquipmentAssignment updated = assignmentRepository.save(assignment);
        return mapToResponse(updated);
    }

    @Override
    public void deleteAssignment(Long id) {
        EquipmentAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        assignmentRepository.delete(assignment);
    }

    private EquipmentAssignmentResponse mapToResponse(EquipmentAssignment assignment) {
        return new EquipmentAssignmentResponse(
                assignment.getId(),
                assignment.getEquipment().getId(),
                assignment.getEquipment().getName(),
                assignment.getWorkerId(),
                assignment.getAssignedDate(),
                assignment.getReturnedDate(),
                assignment.getStatus()
        );
    }
}
