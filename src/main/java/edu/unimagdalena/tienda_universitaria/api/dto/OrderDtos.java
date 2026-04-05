package edu.unimagdalena.tienda_universitaria.api.dto;

import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderDtos {

    public record OrderCreateItemRequest(
            Long productId,
            Integer quantity
    ) implements Serializable {}

    public record OrderCreateRequest(
            Long customerId,
            Long addressId,
            List<OrderCreateItemRequest> items
    ) implements Serializable {}

    public record OrderCancelRequest(
    ) implements Serializable {}

    public record OrderItemResponse(
            Long id,
            Long orderId,
            Long productId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal subTotal,
            Instant createdAt
    ) implements Serializable {}

    public record OrderResponse(
            Long id,
            Long customerId,
            Long addressId,
            OrderStatus status,
            BigDecimal total,
            List<OrderItemResponse> items,
            Instant createdAt,
            Instant updatedAt
    ) implements Serializable {}

}
