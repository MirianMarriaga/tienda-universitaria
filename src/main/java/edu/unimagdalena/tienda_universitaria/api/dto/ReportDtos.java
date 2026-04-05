package edu.unimagdalena.tienda_universitaria.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportDtos {

    public record BestSellingProductResponse(
            Long productId,
            String productName,
            Long totalQuantitySold
    )implements Serializable {}

    public record MonthlyIncomeResponse(
            Integer year,
            Integer mont,
            BigDecimal totalIncome
    ) implements Serializable {}

    public record TopCustomerResponse(
            Long customerId,
            String customerName,
            BigDecimal totalSpent
    )implements Serializable {}
}
