package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.CustomerDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Customer;
import edu.unimagdalena.tienda_universitaria.entities.Order;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import edu.unimagdalena.tienda_universitaria.repositories.CustomerRepository;
import edu.unimagdalena.tienda_universitaria.repositories.OrderRepository;
import edu.unimagdalena.tienda_universitaria.services.mapper.ICustomerMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository repo;
    private final ICustomerMapper mapper;
    private final OrderRepository orderRepo;

    @Transactional
    @Override public CustomerResponse create(CustomerCreateRequest req) {
        var customerEntity = mapper.toEntity(req);
        var entitySaved = repo.save(customerEntity);
        var customerDtoResponse = mapper.toResponse(entitySaved);
        return customerDtoResponse;
    }

    @Override @Transactional(readOnly = true)
    public CustomerResponse get(Long id) {
        return repo.findById(id).map(c-> mapper.toResponse(c))
                .orElseThrow(()-> new RuntimeException("Customer %d not found".formatted(id)));
    }

    @Override @Transactional
    public CustomerResponse update(Long id, CustomerUpdateRequest req) {
        var c = repo.findById(id)
                .orElseThrow(()-> new RuntimeException("Customer %d not found".formatted(id)));
        mapper.patch(c,req);
        return mapper.toResponse(c);
    }

    @Override
    public void deactivate(Long id) {
        Customer c = repo.findById(id)
                .orElseThrow(()-> new RuntimeException("Customer %d not found".formatted(id)));

        List<Order> orders = orderRepo.findByCustomer_Id(id);

        boolean hasActiveOrders = orders.stream()
                .anyMatch(o -> o.getStatus() != OrderStatus.CANCELLED && o.getStatus() != OrderStatus.DELIVERED);
        if(hasActiveOrders) {
            throw new RuntimeException("Customer has active orders");
        }

        c.setStatus(CustomerStatus.INACTIVE);
        repo.save(c);
    }

    @Override @Transactional(readOnly = true)
    public List<CustomerResponse> list() {
        return repo.findAll().stream().map(c-> mapper.toResponse(c)).toList();
    }

    @Override @Transactional(readOnly = true)
    public CustomerResponse findByEmail(String email){
        return repo.findByEmail(email)
                .map(c-> mapper.toResponse(c))
                .orElseThrow(()-> new RuntimeException("Customer with email %s not found".formatted(email)));//me sale rojo el c
    }

    @Override @Transactional(readOnly = true)
    public CustomerResponse findByIdentificationNumber(String identificationNumber) {
        return repo.findByIdentificationNumber(identificationNumber)
                .map(c-> mapper.toResponse(c))
                .orElseThrow(()-> new RuntimeException("Customer with if numer %s not found".formatted(identificationNumber)));
    }

    @Override @Transactional(readOnly = true)
    public List<CustomerResponse> findByStatus(CustomerStatus status) {
        return repo.findByStatus(status)
                .stream()
                .map(c-> mapper.toResponse(c))
                .toList();
    }

}
