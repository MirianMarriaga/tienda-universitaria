package edu.unimagdalena.tienda_universitaria.api.dto;

import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class ReportDtos {

    public record BestSellingProductResponse(
            Long productId,
            String productName,
            Long totalQuantitySold
    )implements Serializable {}

    public record MonthlyIncomeResponse(
            Integer year,
            Integer month,
            BigDecimal totalIncome
    ) implements Serializable {}

    public record TopCustomerResponse(
            Long customerId,
            String customerName,
            BigDecimal totalSpent
    )implements Serializable {}

    public record LowStockProductResponse(
            Long productId,
            String productName,
            Integer availableStock,
            Integer minimumStock
    ) implements Serializable {}

    public record OrderStatusHistoryResponse(
            Long id,
            Long orderId,
            OrderStatus status,
            Instant changedAt
    ) implements Serializable {}

}
