package com.wms.supplyservice.repository;

import com.wms.supplyservice.entity.Supplier;
import com.wms.supplyservice.entity.SupplierStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    Optional<Supplier> findBySupplierCode(String supplierCode);

    List<Supplier> findByStatus(SupplierStatus status);

    long countBySupplierCodeStartingWith(String prefix);

    Optional<Supplier> findTopByOrderBySupplierCodeDesc();
}
