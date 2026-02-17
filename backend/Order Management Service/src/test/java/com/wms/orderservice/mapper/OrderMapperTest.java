package com.wms.orderservice.mapper;

import com.wms.orderservice.dto.request.CreateOrderItemRequest;
import com.wms.orderservice.dto.request.CreateOrderRequest;
import com.wms.orderservice.dto.response.OrderItemResponse;
import com.wms.orderservice.dto.response.OrderResponse;
import com.wms.orderservice.entity.Order;
import com.wms.orderservice.entity.OrderItem;
import com.wms.orderservice.entity.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderMapper = new OrderMapper();
    }

    @Test
    @DisplayName("Should map CreateOrderRequest to Order entity")
    void toEntity_shouldMapCorrectly() {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .customerId("CUST-LK-001")
                .partialAllowed(true)
                .items(List.of(
                        CreateOrderItemRequest.builder()
                                .itemId("ITEM-001")
                                .quantity(10)
                                .unitPrice(new BigDecimal("250.00"))
                                .build(),
                        CreateOrderItemRequest.builder()
                                .itemId("ITEM-002")
                                .quantity(5)
                                .build()
                ))
                .build();

        Order order = orderMapper.toEntity(request);

        assertEquals("CUST-LK-001", order.getCustomerId());
        assertTrue(order.isPartialAllowed());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(2, order.getItems().size());
        assertEquals("ITEM-001", order.getItems().get(0).getItemId());
        assertEquals(10, order.getItems().get(0).getRequestedQty());
        assertEquals(0, order.getItems().get(0).getApprovedQty());
        assertEquals(new BigDecimal("250.00"), order.getItems().get(0).getUnitPrice());
        assertNull(order.getItems().get(1).getUnitPrice());
        // Verify bidirectional relationship
        assertSame(order, order.getItems().get(0).getOrder());
        assertSame(order, order.getItems().get(1).getOrder());
    }

    @Test
    @DisplayName("Should map Order entity to OrderResponse")
    void toResponse_shouldMapCorrectly() {
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        OrderItem item = OrderItem.builder()
                .id(itemId)
                .itemId("ITEM-001")
                .requestedQty(10)
                .approvedQty(8)
                .unitPrice(new BigDecimal("250.00"))
                .build();

        Order order = Order.builder()
                .id(orderId)
                .orderNumber("ORD-2026-000001")
                .customerId("CUST-LK-001")
                .status(OrderStatus.APPROVED)
                .partialAllowed(true)
                .totalAmount(new BigDecimal("2000.00"))
                .createdAt(now)
                .updatedAt(now)
                .items(List.of(item))
                .build();

        OrderResponse response = orderMapper.toResponse(order);

        assertEquals(orderId, response.id());
        assertEquals("ORD-2026-000001", response.orderNumber());
        assertEquals("CUST-LK-001", response.customerId());
        assertEquals(OrderStatus.APPROVED, response.status());
        assertTrue(response.partialAllowed());
        assertEquals(new BigDecimal("2000.00"), response.totalAmount());
        assertEquals(1, response.items().size());

        OrderItemResponse itemResponse = response.items().get(0);
        assertEquals(itemId, itemResponse.id());
        assertEquals("ITEM-001", itemResponse.itemId());
        assertEquals(10, itemResponse.requestedQty());
        assertEquals(8, itemResponse.approvedQty());
    }

    @Test
    @DisplayName("Should handle null items list in toResponse")
    void toResponse_nullItems_shouldReturnEmptyList() {
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-2026-000001")
                .customerId("CUST-LK-001")
                .status(OrderStatus.CREATED)
                .items(null)
                .build();

        OrderResponse response = orderMapper.toResponse(order);
        assertNotNull(response.items());
        assertTrue(response.items().isEmpty());
    }

    @Test
    @DisplayName("Should map list of Orders to list of OrderResponses")
    void toResponseList_shouldMapAll() {
        Order order1 = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-2026-000001")
                .customerId("CUST-LK-001")
                .status(OrderStatus.CREATED)
                .items(List.of())
                .build();

        Order order2 = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-2026-000002")
                .customerId("CUST-LK-002")
                .status(OrderStatus.APPROVED)
                .items(List.of())
                .build();

        List<OrderResponse> responses = orderMapper.toResponseList(List.of(order1, order2));

        assertEquals(2, responses.size());
        assertEquals("ORD-2026-000001", responses.get(0).orderNumber());
        assertEquals("ORD-2026-000002", responses.get(1).orderNumber());
    }
}
