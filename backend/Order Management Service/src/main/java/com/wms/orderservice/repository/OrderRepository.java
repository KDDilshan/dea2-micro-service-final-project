package com.wms.orderservice.repository;

import com.wms.orderservice.entity.Order;
import com.wms.orderservice.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByCustomerId(String customerId);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(o.orderNumber, 10) AS int)), 0) FROM Order o WHERE o.orderNumber LIKE :prefix")
    int findMaxSequenceByPrefix(String prefix);
}
