package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.CustomerDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Customer;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import edu.unimagdalena.tienda_universitaria.repositories.CustomerRepository;
import edu.unimagdalena.tienda_universitaria.repositories.OrderRepository;
import edu.unimagdalena.tienda_universitaria.services.mapper.ICustomerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {
    @Mock
    CustomerRepository customerRepo;
    @Mock
    OrderRepository orderRepo;
    @InjectMocks
    CustomerServiceImpl service;
    @Spy
    private ICustomerMapper mapper = Mappers.getMapper(ICustomerMapper.class);

    @Test
    void shouldCreateAndReturnResponseDto() {
        var req = new CustomerCreateRequest(
                "1001234567",
                "Juan Amador Hernandez",
                "jahernandez@unimagdalena.edu.co",
                "+57 310 456 7821",
                null
        );

        when(customerRepo.save(any())).thenAnswer(inv -> {
            Customer c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        var res = service.create(req);

        assertThat(res.id()).isEqualTo(1L);
        assertThat(res.email()).isEqualTo("jahernandez@unimagdalena.edu.co");
        assertThat(res.status()).isEqualTo(CustomerStatus.ACTIVE);
        verify(customerRepo).save(any(Customer.class));
    }

    @Test
    void shouldGetCustomerById() {
        var entity = Customer.builder()
                .id(1L)
                .email("jahernandez@unimagdalena.edu.co")
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(customerRepo.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.get(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("jahernandez@unimagdalena.edu.co");
    }

    @Test
    void shouldUpdateViaPatch() {
        var entity = Customer.builder()
                .id(1L)
                .email("jahernandez@unimagdalena.edu.co")
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(customerRepo.findById(1L)).thenReturn(Optional.of(entity));
        when(customerRepo.save(any())).thenReturn(entity);

        var req = new CustomerUpdateRequest(
                "Juan Amador Nino",
                null,
                null,
                null
        );

        var result = service.update(1L, req);

        assertThat(result.fullName()).isEqualTo("Juan Amador Nino");
        assertThat(result.email()).isEqualTo("jahernandez@unimagdalena.edu.co");
    }

    @Test
    void shouldDeactivateCustomerWhenNoActiveOrders() {
        var customer = Customer.builder()
                .id(1L)
                .email("jahernandez@unimagdalena.edu.co")
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepo.findByCustomer_Id(1L)).thenReturn(List.of());

        service.deactivate(1L);

        assertThat(customer.getStatus()).isEqualTo(CustomerStatus.INACTIVE);
        verify(customerRepo).save(customer);
    }

    @Test
    void shouldListAllCustomers() {
        var page = new PageImpl<>(java.util.List.of(
                Customer.builder()
                        .email("jahernandez@unimagdalena.edu.co")
                        .fullName("Juan Amador Hernandez")
                        .identificationNumber("1001234567")
                        .phone("+57 310 456 7821")
                        .status(CustomerStatus.ACTIVE)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        ));
        when(customerRepo.findAll(PageRequest.of(0, 5))).thenReturn(page);

        var result = service.list(PageRequest.of(0, 5));
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).email()).isEqualTo("jahernandez@unimagdalena.edu.co");
    }

    @Test
    void shouldFindByEmail() {
        var entity = Customer.builder()
                .id(1L)
                .email("jahernandez@unimagdalena.edu.co")
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(customerRepo.findByEmail("jahernandez@unimagdalena.edu.co")).thenReturn(Optional.of(entity));

        var result = service.findByEmail("jahernandez@unimagdalena.edu.co");

        assertThat(result.email()).isEqualTo("jahernandez@unimagdalena.edu.co");
    }

    @Test
    void shouldFindByIdentificationNumber() {
        var entity = Customer.builder()
                .email("jahernandez@unimagdalena.edu.co")
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(customerRepo.findByIdentificationNumber("1001234567"))
                .thenReturn(Optional.of(entity));

        var result = service.findByIdentificationNumber("1001234567");

        assertThat(result.fullName()).isEqualTo("Juan Amador Hernandez");
    }

    @Test
    void shouldFindByStatus() {
        var entity = Customer.builder()
                .email("jahernandez@unimagdalena.edu.co")
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(customerRepo.findByStatus(CustomerStatus.ACTIVE))
                .thenReturn(List.of(entity));

        var result = service.findByStatus(CustomerStatus.ACTIVE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(CustomerStatus.ACTIVE);
    }
}
