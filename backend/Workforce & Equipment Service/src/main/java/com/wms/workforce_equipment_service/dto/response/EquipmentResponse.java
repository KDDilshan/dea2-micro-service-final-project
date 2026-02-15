package com.wms.workforce_equipment_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentResponse {

    private Long id;
    private Long userId;
    private String type;
    private String name;
}
