package com.wms.inbound_receiving_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inbound_shipments")
@Getter
@Setter
public class InboundShipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String supplierName;
    private String productName;
    private int quantity;
    private String status;
}
