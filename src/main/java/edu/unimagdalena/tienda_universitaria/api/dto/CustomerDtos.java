package edu.unimagdalena.tienda_universitaria.api.dto;

import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class CustomerDtos {

    public record CustomerCreateRequest(
            @NotBlank String identificationNumber,
            @NotBlank String fullName,
            @NotBlank @Email String email,
            @NotBlank String phone,
            @NotNull CustomerStatus status
    ) implements Serializable {}

    public record CustomerUpdateRequest(
            String fullName,
            String email,
            String phone,
            CustomerStatus status
    ) implements Serializable {}

    public record CustomerResponse(
            Long id,
            String fullName,
            String identificationNumber,
            String email,
            String phone,
            CustomerStatus status,
            Instant createdAt,
            Instant updatedAt
    ) implements Serializable {}


}
