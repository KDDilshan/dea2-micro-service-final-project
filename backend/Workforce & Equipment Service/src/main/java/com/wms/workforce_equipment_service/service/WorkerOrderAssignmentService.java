package com.wms.workforce_equipment_service.service;

import com.wms.workforce_equipment_service.client.OrderServiceClient;
import com.wms.workforce_equipment_service.dto.request.AssignWorkersToOrderRequest;
import com.wms.workforce_equipment_service.dto.request.UpdateWorkerRequest;
import com.wms.workforce_equipment_service.dto.response.WorkerOrderAssignmentResponse;
import com.wms.workforce_equipment_service.exception.ResourceNotFoundException;
import com.wms.workforce_equipment_service.model.Worker;
import com.wms.workforce_equipment_service.model.WorkerOrderAssignment;
import com.wms.workforce_equipment_service.repository.WorkerOrderAssignmentRepository;
import com.wms.workforce_equipment_service.repository.WorkerRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerOrderAssignmentService implements IWorkerOrderAssignmentService {

    private final WorkerOrderAssignmentRepository workerOrderAssignmentRepository;
    private final WorkerRepository workerRepository;
    private final OrderServiceClient orderServiceClient;

    /**
     * Assigns workers and a supervisor to an order.
     *
     * @param request the assignment request containing order ID, supervisor ID, and worker IDs
     * @return a list of worker order assignment responses
     * @throws ResourceNotFoundException if the supervisor, any worker, or the order is not found
     * @throws RuntimeException if there is an error communicating with the Order Service
     */
    @Override
    @Transactional
    public List<WorkerOrderAssignmentResponse> assignWorkersToOrder(AssignWorkersToOrderRequest request) {
        log.info("Assigning supervisor {} and {} workers to order {}",
                request.getSupervisorId(), request.getWorkerIds().size(), request.getOrderId());

        // Validate supervisor exists
        Worker supervisor = workerRepository.findById(request.getSupervisorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supervisor not found with id: " + request.getSupervisorId()));

        // Assign supervisor to the order via Feign call to Order Service
        try {
            UpdateWorkerRequest updateWorkerRequest = new UpdateWorkerRequest(
                    String.valueOf(supervisor.getId()));
            orderServiceClient.updateWorkerId(request.getOrderId(), updateWorkerRequest);
            log.info("Successfully assigned supervisor {} to order {} via Order Service",
                    supervisor.getId(), request.getOrderId());
        } catch (FeignException.NotFound e) {
            log.error("Order {} not found in Order Service", request.getOrderId());
            throw new ResourceNotFoundException("Order not found with id: " + request.getOrderId());
        } catch (FeignException e) {
            log.error("Error communicating with Order Service", e);
            throw new RuntimeException("Error communicating with Order Service: " + e.getMessage());
        }

        // Create worker-order assignments for each worker
        List<WorkerOrderAssignment> assignments = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Long workerId : request.getWorkerIds()) {
            Worker worker = workerRepository.findById(workerId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Worker not found with id: " + workerId));

            // Skip if already assigned
            if (workerOrderAssignmentRepository.existsByWorkerAndOrderId(worker, request.getOrderId())) {
                log.warn("Worker {} is already assigned to order {}, skipping",
                        workerId, request.getOrderId());
                continue;
            }

            WorkerOrderAssignment assignment = new WorkerOrderAssignment();
            assignment.setWorker(worker);
            assignment.setOrderId(request.getOrderId());
            assignment.setAssignedDate(now);
            assignments.add(assignment);
        }

        List<WorkerOrderAssignment> savedAssignments = workerOrderAssignmentRepository.saveAll(assignments);

        return savedAssignments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all worker assignments for a specific order.
     *
     * @param orderId the ID of the order
     * @return a list of worker order assignment responses
     */
    @Override
    public List<WorkerOrderAssignmentResponse> getAssignmentsByOrderId(String orderId) {
        List<WorkerOrderAssignment> assignments = workerOrderAssignmentRepository.findByOrderId(orderId);
        return assignments.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Retrieves all order assignments for a specific worker.
     *
     * @param workerId the ID of the worker
     * @return a list of worker order assignment responses
     * @throws ResourceNotFoundException if the worker is not found
     */
    @Override
    public List<WorkerOrderAssignmentResponse> getAssignmentsByWorkerId(Long workerId) {
        if (!workerRepository.existsById(workerId)) {
            throw new ResourceNotFoundException("Worker not found with id: " + workerId);
        }

        List<WorkerOrderAssignment> assignments = workerOrderAssignmentRepository.findByWorkerId(workerId);
        return assignments.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Maps a WorkerOrderAssignment entity to a WorkerOrderAssignmentResponse DTO.
     *
     * @param entity the worker order assignment entity
     * @return the mapped worker order assignment response
     */
    private WorkerOrderAssignmentResponse mapToResponse(WorkerOrderAssignment entity) {
        return new WorkerOrderAssignmentResponse(
                entity.getId(),
                entity.getWorker().getId(),
                entity.getOrderId(),
                entity.getAssignedDate()
        );
    }
}
