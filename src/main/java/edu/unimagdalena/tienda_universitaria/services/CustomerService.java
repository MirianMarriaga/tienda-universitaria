package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.CustomerDtos;

import java.util.List;

public interface CustomerService {

    CustomerDtos.CustomerResponse create(CustomerDtos.CustomerCreateRequest req);
    CustomerDtos.CustomerResponse get(Long id);
    CustomerDtos.CustomerResponse update(Long id, CustomerDtos.CustomerUpdateRequest req);
    List<CustomerDtos.CustomerResponse> list();

}
