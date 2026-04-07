package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.AddressDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Address;
import edu.unimagdalena.tienda_universitaria.repositories.AddressRepository;
import edu.unimagdalena.tienda_universitaria.repositories.CustomerRepository;
import edu.unimagdalena.tienda_universitaria.services.mapper.IAddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor

public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepo;
    private final CustomerRepository customerRepo;
    private final IAddressMapper mapper;


    @Override
    public AddressResponse create(Long customerId, AddressCreateRequest req) {
        var customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer %d not found".formatted(customerId)));

        Address address = mapper.toEntity(req);
        address.setCustomer(customer);
        address.setCreatedAt(Instant.now());
        return mapper.toResponse(addressRepo.save(address));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> listByCustomer(Long customerId) {
        return addressRepo.findByCustomer_Id(customerId).stream()
                .map(mapper::toResponse)
                .toList();
    }

}
