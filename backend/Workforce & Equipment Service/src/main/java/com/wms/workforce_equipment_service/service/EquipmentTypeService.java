package com.wms.workforce_equipment_service.service;

import com.wms.workforce_equipment_service.dto.request.EquipmentTypeRequest;
import com.wms.workforce_equipment_service.dto.response.EquipmentTypeResponse;
import com.wms.workforce_equipment_service.exception.ResourceNotFoundException;
import com.wms.workforce_equipment_service.model.EquipmentType;
import com.wms.workforce_equipment_service.repository.EquipmentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing equipment types.
 */
@Service
@RequiredArgsConstructor
public class EquipmentTypeService implements IEquipmentTypeService {

    private final EquipmentTypeRepository equipmentTypeRepository;

    /**
     * Retrieves all equipment types.
     *
     * @return a list of equipment type responses
     */
    @Override
    public List<EquipmentTypeResponse> getAllEquipmentTypes() {
        return equipmentTypeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an equipment type by its ID.
     *
     * @param id the ID of the equipment type to retrieve
     * @return the equipment type response
     * @throws ResourceNotFoundException if the equipment type is not found
     */
    @Override
    public EquipmentTypeResponse getEquipmentTypeById(Long id) {
        EquipmentType type = equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment Type not found with id: " + id));
        return mapToResponse(type);
    }

    /**
     * Creates a new equipment type.
     *
     * @param request the equipment type request containing the details
     * @return the created equipment type response
     */
    @Override
    public EquipmentTypeResponse createEquipmentType(EquipmentTypeRequest request) {
        EquipmentType type = new EquipmentType();
        type.setName(request.getName());
        type.setDescription(request.getDescription());
        type.setManufacturer(request.getManufacturer());
        type.setModel(request.getModel());

        EquipmentType saved = equipmentTypeRepository.save(type);
        return mapToResponse(saved);
    }

    /**
     * Updates an existing equipment type.
     *
     * @param id the ID of the equipment type to update
     * @param request the equipment type request containing the updated details
     * @return the updated equipment type response
     * @throws ResourceNotFoundException if the equipment type is not found
     */
    @Override
    public EquipmentTypeResponse updateEquipmentType(Long id, EquipmentTypeRequest request) {
        EquipmentType type = equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment Type not found with id: " + id));

        type.setName(request.getName());
        type.setDescription(request.getDescription());
        type.setManufacturer(request.getManufacturer());
        type.setModel(request.getModel());

        EquipmentType updated = equipmentTypeRepository.save(type);
        return mapToResponse(updated);
    }

    /**
     * Deletes an equipment type by its ID.
     *
     * @param id the ID of the equipment type to delete
     * @throws ResourceNotFoundException if the equipment type is not found
     */
    @Override
    public void deleteEquipmentType(Long id) {
        EquipmentType type = equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment Type not found with id: " + id));
        equipmentTypeRepository.delete(type);
    }

    /**
     * Maps an EquipmentType entity to an EquipmentTypeResponse DTO.
     *
     * @param type the equipment type entity
     * @return the mapped equipment type response
     */
    private EquipmentTypeResponse mapToResponse(EquipmentType type) {
        return new EquipmentTypeResponse(
                type.getId(),
                type.getName(),
                type.getDescription(),
                type.getManufacturer(),
                type.getModel()
        );
    }
}
