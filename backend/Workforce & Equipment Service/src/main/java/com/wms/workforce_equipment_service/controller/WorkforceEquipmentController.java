package com.wms.workforce_equipment_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workforce-equipment")
public class WorkforceEquipmentController {

    @GetMapping("/hello")
    public String sayHi() {
        return "Hi";
    }
}
