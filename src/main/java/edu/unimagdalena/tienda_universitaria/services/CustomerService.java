package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.CustomerDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerService {

    CustomerResponse create(CustomerCreateRequest req);
    CustomerResponse get(Long id);
    CustomerResponse update(Long id, CustomerUpdateRequest req);
    void deactivate(Long id);
    Page<CustomerResponse> list(Pageable pageable);
    CustomerResponse findByEmail(String email);
    CustomerResponse findByIdentificationNumber(String identificationNumber);
    List<CustomerResponse> findByStatus(CustomerStatus status);



}
