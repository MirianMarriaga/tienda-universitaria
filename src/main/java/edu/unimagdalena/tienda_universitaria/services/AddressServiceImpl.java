package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.AddressDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Address;
import edu.unimagdalena.tienda_universitaria.exception.ResourceNotFoundException;
import edu.unimagdalena.tienda_universitaria.exception.ValidationException;
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

        if (req.street() == null || req.street().isBlank()) {
            throw new ValidationException("Street is required");
        }

        var customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer %d not found".formatted(customerId)));

        var address = mapper.toEntity(req);
        address.setCustomer(customer);
        address.setCreatedAt(Instant.now());

        var saved = addressRepo.save(address);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> listByCustomer(Long customerId) {

        if (!customerRepo.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer %d not found".formatted(customerId));
        }
        var addresses = addressRepo.findByCustomer_Id(customerId);


        return addresses.stream()
                .map(mapper::toResponse)
                .toList();
    }

    }


