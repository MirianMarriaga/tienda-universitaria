package edu.unimagdalena.tienda_universitaria.api.dto;

import java.io.Serializable;
import java.time.Instant;

public class AddressDtos {

    public record AddressCreateRequest(
            Long customerId,
            String street,
            String city,
            String state,
            String country
    ) implements Serializable {}


    public record AddressResponse(
            Long id,
            Long customerId,
            String street,
            String city,
            String state,
            String country,
            Instant createdAt,
            Instant updatedAt
    ) implements Serializable {}
}
