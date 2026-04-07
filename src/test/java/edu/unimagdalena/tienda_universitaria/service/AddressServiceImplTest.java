package edu.unimagdalena.tienda_universitaria.service;

import edu.unimagdalena.tienda_universitaria.api.dto.AddressDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Address;
import edu.unimagdalena.tienda_universitaria.entities.Customer;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import edu.unimagdalena.tienda_universitaria.repositories.AddressRepository;
import edu.unimagdalena.tienda_universitaria.repositories.CustomerRepository;
import edu.unimagdalena.tienda_universitaria.services.AddressServiceImpl;
import edu.unimagdalena.tienda_universitaria.services.mapper.IAddressMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @Mock AddressRepository addressRepo;
    @Mock CustomerRepository customerRepo;
    @Mock IAddressMapper mapper;

    @InjectMocks
    AddressServiceImpl service;

    @Test
    void shouldCreateAddress() {
        // Given
        var customer = Customer.builder()
                .id(1L).fullName("Angelica Villegas")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now()).updatedAt(Instant.now())
                .build();

        var req = new AddressCreateRequest(1L, "Street 22", "Santa Marta", "Magdalena", "Colombia");

        var address = Address.builder()
                .id(1L).customer(customer)
                .street("Street 22").city("Santa Marta")
                .state("Magdalena").country("Colombia")
                .createdAt(Instant.now())
                .build();

        var response = new AddressResponse(1L, 1L, "Street 22", "Santa Marta", "Magdalena", "Colombia", Instant.now());

        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(mapper.toEntity(req)).thenReturn(address);
        when(addressRepo.save(any())).thenReturn(address);
        when(mapper.toResponse(address)).thenReturn(response);

        // When
        var result = service.create(1L, req);

        // Then
        assertThat(result.customerId()).isEqualTo(1L);
        assertThat(result.city()).isEqualTo("Santa Marta");
    }

    @Test
    void shouldThrowWhenCustomerNotFound() {
        // Given
        var req = new AddressCreateRequest(99L, "Street 22", "Santa Marta", "Magdalena", "Colombia");
        when(customerRepo.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.create(99L, req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldListAddressesByCustomer() {
        // Given
        var customer = Customer.builder().id(1L).fullName("Angelica Villegas").build();
        var address = Address.builder().id(1L).customer(customer).city("Santa Marta").build();
        var response = new AddressResponse(1L, 1L, "Street 22", "Santa Marta", "Magdalena", "Colombia", Instant.now());

        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(addressRepo.findByCustomer_Id(1L)).thenReturn(List.of(address));
        when(mapper.toResponse(address)).thenReturn(response);

        // When
        var result = service.listByCustomer(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).city()).isEqualTo("Santa Marta");
    }

    @Test
    void shouldThrowWhenCustomerNotFoundOnList() {
        // Given
        when(customerRepo.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.listByCustomer(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }
}