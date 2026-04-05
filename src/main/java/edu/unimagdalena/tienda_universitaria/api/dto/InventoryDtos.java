package edu.unimagdalena.tienda_universitaria.api.dto;

import java.io.Serializable;
import java.time.Instant;

public class InventoryDtos {

    public record InventoryUpdateRequest(
            Integer availableStock,
            Integer minimumStock
    ) implements Serializable {}

    public record InventoryResponse(
            Long id,
            Long productId,
            Integer availableStock,
            Integer minimumStock,
            Instant updatedAt
    ) implements Serializable {}
}
