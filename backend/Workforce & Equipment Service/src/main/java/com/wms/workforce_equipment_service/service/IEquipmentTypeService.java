package com.wms.workforce_equipment_service.service;

import com.wms.workforce_equipment_service.dto.request.EquipmentTypeRequest;
import com.wms.workforce_equipment_service.dto.response.EquipmentTypeResponse;

import java.util.List;

public interface IEquipmentTypeService {

    List<EquipmentTypeResponse> getAllEquipmentTypes();

    EquipmentTypeResponse getEquipmentTypeById(Long id);

    EquipmentTypeResponse createEquipmentType(EquipmentTypeRequest request);

    EquipmentTypeResponse updateEquipmentType(Long id, EquipmentTypeRequest request);

    void deleteEquipmentType(Long id);
}
