package com.wms.workforce_equipment_service.service;

import com.wms.workforce_equipment_service.client.StorageLocationServiceClient;
import com.wms.workforce_equipment_service.dto.request.WorkerStorageLocationRequest;
import com.wms.workforce_equipment_service.dto.response.StorageLocationResponse;
import com.wms.workforce_equipment_service.dto.response.WorkerStorageLocationResponse;
import com.wms.workforce_equipment_service.exception.ConflictException;
import com.wms.workforce_equipment_service.exception.ResourceNotFoundException;
import com.wms.workforce_equipment_service.model.Worker;
import com.wms.workforce_equipment_service.model.WorkerStorageLocation;
import com.wms.workforce_equipment_service.repository.WorkerRepository;
import com.wms.workforce_equipment_service.repository.WorkerStorageLocationRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerStorageLocationService implements IWorkerStorageLocationService {

    private final WorkerStorageLocationRepository workerStorageLocationRepository;
    private final WorkerRepository workerRepository;
    private final StorageLocationServiceClient storageLocationServiceClient;

    /**
     * Assigns a worker to a storage location.
     *
     * @param request the assignment request containing worker ID and storage location ID
     * @return the worker storage location response
     * @throws ResourceNotFoundException if the worker or storage location is not found
     * @throws ConflictException if the worker is already assigned to the storage location
     * @throws RuntimeException if there is an error communicating with the Storage Location Service
     */
    @Override
    @Transactional
    public WorkerStorageLocationResponse assignWorkerToStorageLocation(WorkerStorageLocationRequest request) {
        log.info("Assigning worker {} to storage location {}", request.getWorkerId(), request.getStorageLocationId());

        //validate Worker exists
        Worker worker = workerRepository.findById(request.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found with id: " + request.getWorkerId()));

        //validate Storage Location exists via Feign Client
        try {
            StorageLocationResponse locationResponse = storageLocationServiceClient.getStorageLocationById(request.getStorageLocationId());
            if (locationResponse == null) {
                // if storage location is not found in inventory service
                throw new ResourceNotFoundException("Storage Location not found with id: " + request.getStorageLocationId());
            }
        } catch (FeignException.NotFound e) {
            log.error("Storage Location {} not found in Storage Location Service", request.getStorageLocationId());
            throw new ResourceNotFoundException("Storage Location not found with id: " + request.getStorageLocationId());
        } catch (FeignException e) {
            log.error("Error communicating with Storage Location Service", e);
            throw new RuntimeException("Error communicating with Storage Location Service: " + e.getMessage());
        }

        //check for existing assignment
        if (workerStorageLocationRepository.existsByWorkerAndStorageLocationId(worker, request.getStorageLocationId())) {
            throw new ConflictException("Worker " + request.getWorkerId() + " is already assigned to storage location " + request.getStorageLocationId());
        }

        // save
        WorkerStorageLocation assignment = new WorkerStorageLocation();
        assignment.setWorker(worker);
        assignment.setStorageLocationId(request.getStorageLocationId());
        assignment.setAssignedDate(LocalDateTime.now());

        WorkerStorageLocation savedAssignment = workerStorageLocationRepository.save(assignment);
        
        return mapToResponse(savedAssignment);
    }

    /**
     * Retrieves all storage location assignments for a specific worker.
     *
     * @param workerId the ID of the worker
     * @return a list of worker storage location responses
     * @throws ResourceNotFoundException if the worker is not found
     */
    @Override
    public List<WorkerStorageLocationResponse> getStorageLocationsByWorkerId(Long workerId) {
        // Validate worker exists first
        if (!workerRepository.existsById(workerId)) {
            throw new ResourceNotFoundException("Worker not found with id: " + workerId);
        }
        
        List<WorkerStorageLocation> assignments = workerStorageLocationRepository.findByWorkerId(workerId);
        return assignments.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Removes a worker storage location assignment by its ID.
     *
     * @param id the ID of the assignment to remove
     * @throws ResourceNotFoundException if the assignment is not found
     */
    @Override
    @Transactional
    public void removeAssignment(Long id) {
        if (!workerStorageLocationRepository.existsById(id)) {
            throw new ResourceNotFoundException("WorkerStorageLocation assignment not found with id: " + id);
        }
        workerStorageLocationRepository.deleteById(id);
    }

    /**
     * Maps a WorkerStorageLocation entity to a WorkerStorageLocationResponse DTO.
     *
     * @param entity the worker storage location entity
     * @return the mapped worker storage location response
     */
    private WorkerStorageLocationResponse mapToResponse(WorkerStorageLocation entity) {
        return new WorkerStorageLocationResponse(
                entity.getId(),
                entity.getWorker().getId(),
                entity.getStorageLocationId(),
                entity.getAssignedDate()
        );
    }
}
