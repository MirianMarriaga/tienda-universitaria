package edu.unimagdalena.tienda_universitaria.api.dto;

import java.io.Serializable;
import java.time.Instant;

public class CategoryDtos {
    public record CategoryCreateRequest(
            String name,
            String description
    ) implements Serializable {}


    public record CategoryResponse(
            Long id,
            String name,
            String description,
            Instant createdAt
    ) implements Serializable {}
}
