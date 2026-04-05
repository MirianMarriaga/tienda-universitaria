package edu.unimagdalena.tienda_universitaria.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class ProductDtos {

    public record ProductCreateRequest(
            String sku,
            Long category,
            String name,
            String description,
            BigDecimal price
    ) implements Serializable {}

    public record ProductUpdateRequest(
            Long category,
            String name,
            String description,
            BigDecimal price,
            Boolean active
    ) implements  Serializable {}

    public record ProductResponse(
            Long id,
            String sku,
            Long categoryId,
            String name,
            String description,
            BigDecimal price,
            Boolean active,
            Instant createdAt,
            Instant updatedAt
    ) implements Serializable {}

    public record LowStockProductResponse(
            Long productId,
            String productName,
            Integer availableStock,
            Integer minimumStock
    ) implements Serializable {}


}
