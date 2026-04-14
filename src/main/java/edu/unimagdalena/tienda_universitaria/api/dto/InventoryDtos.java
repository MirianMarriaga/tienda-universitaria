package edu.unimagdalena.tienda_universitaria.api.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

public class InventoryDtos {

    public record InventoryUpdateRequest(
            @Min(0) Integer availableStock,
            @Min(0) Integer minimumStock
    ) implements Serializable {}

    public record InventoryResponse(
            Long id,
            Long productId,
            Integer availableStock,
            Integer minimumStock,
            Instant updatedAt
    ) implements Serializable {}
}
