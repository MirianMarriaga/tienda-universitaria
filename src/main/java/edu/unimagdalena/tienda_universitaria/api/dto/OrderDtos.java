package edu.unimagdalena.tienda_universitaria.api.dto;

import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderDtos {

    public record OrderCreateItemRequest(
            @NotNull @Positive Long productId,
            @NotNull @Min(1) Integer quantity
    ) implements Serializable {}

    public record OrderCreateRequest(
            @NotNull @Positive Long customerId,
            @NotNull @Positive Long addressId,
            @Valid List<OrderCreateItemRequest> items
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
            BigDecimal subtotal,
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

