package com.wms.workforce_equipment_service.service;

import com.wms.workforce_equipment_service.dto.request.EquipmentRequest;
import com.wms.workforce_equipment_service.dto.response.EquipmentResponse;
import com.wms.workforce_equipment_service.exception.ResourceNotFoundException;
import com.wms.workforce_equipment_service.model.Equipment;
import com.wms.workforce_equipment_service.model.EquipmentType;
import com.wms.workforce_equipment_service.repository.EquipmentRepository;
import com.wms.workforce_equipment_service.repository.EquipmentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing equipment.
 */
@Service
@RequiredArgsConstructor
public class EquipmentService implements IEquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;

    /**
     * Retrieves all equipment.
     *
     * @return a list of equipment responses
     */
    @Override
    public List<EquipmentResponse> getAllEquipments() {
        return equipmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an equipment by its ID.
     *
     * @param id the ID of the equipment to retrieve
     * @return the equipment response
     * @throws ResourceNotFoundException if the equipment is not found
     */
    @Override
    public EquipmentResponse getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        return mapToResponse(equipment);
    }

    /**
     * Creates a new equipment.
     *
     * @param request the equipment request containing the details
     * @return the created equipment response
     * @throws ResourceNotFoundException if the associated equipment type is not found
     */
    @Override
    public EquipmentResponse createEquipment(EquipmentRequest request) {
        EquipmentType equipmentType = equipmentTypeRepository.findById(request.getEquipmentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment Type not found with id: " + request.getEquipmentTypeId()));

        Equipment equipment = new Equipment();
        equipment.setName(request.getName());
        equipment.setStatus(request.getStatus());
        equipment.setDescription(request.getDescription());
        equipment.setEquipmentType(equipmentType);

        Equipment saved = equipmentRepository.save(equipment);
        return mapToResponse(saved);
    }

    /**
     * Updates an existing equipment.
     *
     * @param id the ID of the equipment to update
     * @param request the equipment request containing the updated details
     * @return the updated equipment response
     * @throws ResourceNotFoundException if the equipment or associated equipment type is not found
     */
    @Override
    public EquipmentResponse updateEquipment(Long id, EquipmentRequest request) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));

        EquipmentType equipmentType = equipmentTypeRepository.findById(request.getEquipmentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment Type not found with id: " + request.getEquipmentTypeId()));

        equipment.setName(request.getName());
        equipment.setStatus(request.getStatus());
        equipment.setDescription(request.getDescription());
        equipment.setEquipmentType(equipmentType);

        Equipment updated = equipmentRepository.save(equipment);
        return mapToResponse(updated);
    }

    /**
     * Deletes an equipment by its ID.
     *
     * @param id the ID of the equipment to delete
     * @throws ResourceNotFoundException if the equipment is not found
     */
    @Override
    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        equipmentRepository.delete(equipment);
    }

    /**
     * Maps an Equipment entity to an EquipmentResponse DTO.
     *
     * @param equipment the equipment entity
     * @return the mapped equipment response
     */
    private EquipmentResponse mapToResponse(Equipment equipment) {
        return new EquipmentResponse(
                equipment.getId(),
                equipment.getName(),
                equipment.getStatus(),
                equipment.getDescription(),
                equipment.getEquipmentType().getId(),
                equipment.getEquipmentType().getName()
        );
    }
}
