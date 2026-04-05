package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Address;
import edu.unimagdalena.tienda_universitaria.entities.Customer;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AddressRepositoryIntegrationTest extends AbstractRepositoryIT{

    @Autowired
    AddressRepository addressRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Test
    @DisplayName("Address: Search by customer id")
    void shouldFindByCustomer_Id(){
        //Given
        var customer1 = customerRepo.save(Customer.builder()
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .email("jahernandez@unimagdalena.edu.co")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var customer2 = customerRepo.save(Customer.builder()
                .fullName("Carlos Andrés Rodríguez")
                .identificationNumber("1007654321")
                .email("crodriguez@unimagdalena.edu.co")
                .phone("+57 315 672 1904")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        addressRepo.save(Address.builder()
                .customer(customer1)
                .street("Calle 22 #5-30")
                .city("Santa Marta")
                .state("Magdalena")
                .country("Colombia")
                .createdAt(Instant.now())
                .build());

        addressRepo.save(Address.builder()
                .customer(customer2)
                .street("Carrera 19 #45-12")
                .city("Barranquilla")
                .state("Atlántico")
                .country("Colombia")
                .createdAt(Instant.now())
                .build());

        //When
        List<Address> result = addressRepo.findByCustomer_Id(customer1.getId());

        //Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomer().getId()).isEqualTo(customer1.getId());

    }
}
