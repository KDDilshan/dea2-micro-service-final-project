package com.wms.workforce_equipment_service.controller;

import com.wms.workforce_equipment_service.dto.request.WorkerRequest;
import com.wms.workforce_equipment_service.dto.response.WorkerResponse;
import com.wms.workforce_equipment_service.service.IWorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workforce-equipment/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final IWorkerService workerService;

    @GetMapping
    public ResponseEntity<List<WorkerResponse>> getAllWorkers() {
        return ResponseEntity.ok(workerService.getAllWorkers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkerResponse> getWorkerById(@PathVariable Long id) {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }

    @PostMapping
    public ResponseEntity<WorkerResponse> createWorker(@Valid @RequestBody WorkerRequest request) {
        WorkerResponse created = workerService.createWorker(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkerResponse> updateWorker(@PathVariable Long id,
                                                        @Valid @RequestBody WorkerRequest request) {
        return ResponseEntity.ok(workerService.updateWorker(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
        return ResponseEntity.noContent().build();
    }
}
