package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.AddressDtos.*;
import edu.unimagdalena.tienda_universitaria.api.dto.CustomerDtos.*;

import java.util.List;

public interface AddressService {

    AddressResponse create(Long customerId, AddressCreateRequest req);
    List<AddressResponse> listByCustomer(Long customerId);

}
