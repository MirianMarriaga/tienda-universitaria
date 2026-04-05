package edu.unimagdalena.tienda_universitaria.services.mapper;

import edu.unimagdalena.tienda_universitaria.api.dto.AddressDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IAddressMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Address toEntity(AddressCreateRequest req);

    @Mapping(target = "customerId", source = "customer.id")
    AddressResponse toResponse(Address a);
}
