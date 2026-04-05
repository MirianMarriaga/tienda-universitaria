package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.AddressDtos;
import edu.unimagdalena.tienda_universitaria.api.dto.CustomerDtos.*;

import java.util.List;

public interface AddressService {

    AddressDtos.AddressResponse create(Long customerId, AddressDtos.AddressCreateRequest req);
    List<AddressDtos.AddressResponse> listByCustomer(Long customerId);

}
