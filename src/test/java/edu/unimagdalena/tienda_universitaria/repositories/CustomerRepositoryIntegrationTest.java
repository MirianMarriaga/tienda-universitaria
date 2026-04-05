package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Address;
import edu.unimagdalena.tienda_universitaria.entities.Customer;
import edu.unimagdalena.tienda_universitaria.entities.Order;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerRepositoryIntegrationTest extends AbstractRepositoryIT{

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    AddressRepository addressRepo;

    @Test
    @DisplayName("Customer: Search by email")
    void shouldFindByEmail(){
        //Given
        var customer1= customerRepo.save(Customer.builder()
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

        //When
        Optional<Customer> result = customerRepo.findByEmail("jahernandez@unimagdalena.edu.co");

        //Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(customer1.getId());
        assertThat(result.get().getEmail()).isEqualTo("jahernandez@unimagdalena.edu.co");
    }

    @Test
    @DisplayName("Customer: Search by status")
    void shouldFindByStatus(){
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
                .status(CustomerStatus.INACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        //When
        List<Customer> result = customerRepo.findByStatus(CustomerStatus.ACTIVE);

        //Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getId()).isEqualTo(customer1.getId());
        assertThat(result.get(0).getStatus()).isEqualTo(CustomerStatus.ACTIVE);
    }

    @Test
    @DisplayName("Customer: Search by identification number")
    void shouldFindByIdentificationNumber(){
        //Given
        var customer1= customerRepo.save(Customer.builder()
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

        //When
        Optional<Customer> result = customerRepo.findByIdentificationNumber("1001234567");

        //Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(customer1.getId());
        assertThat(result.get().getIdentificationNumber()).isEqualTo(customer1.getIdentificationNumber());

    }

    @Test
    @DisplayName("Customer: Search top by billing")
    void findTopByCustomer(){
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
                .status(CustomerStatus.INACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var address1 = addressRepo.save(Address.builder()
                .customer(customer1)
                .street("Calle 22 #5-30")
                .city("Santa Marta")
                .state("Magdalena")
                .country("Colombia")
                .createdAt(Instant.now())
                .build());

        var address2 = addressRepo.save(Address.builder()
                .customer(customer2)
                .street("Carrera 19 #45-12")
                .city("Barranquilla")
                .state("Atlántico")
                .country("Colombia")
                .createdAt(Instant.now())
                .build());

        orderRepo.save(Order.builder()
                .customer(customer1)
                .address(address1)
                .status(OrderStatus.PAID)
                .total(new BigDecimal(430000.00))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        orderRepo.save(Order.builder()
                .customer(customer1)
                .address(address1)
                .status(OrderStatus.PAID)
                .total(new BigDecimal(60000.00))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        orderRepo.save(Order.builder()
                .customer(customer2)
                .address(address2)
                .status(OrderStatus.PAID)
                .total(new BigDecimal(200000.00))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        orderRepo.save(Order.builder()
                .customer(customer2)
                .address(address2)
                .status(OrderStatus.PAID)
                .total(new BigDecimal(800000.00))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());



        //When
        List<Object[]> result = customerRepo.findTopByCustomer();

        //Then
        assertThat(result).hasSize(2);
        assertThat((String) result.get(0)[1]).isEqualTo("Carlos Andrés Rodríguez");

    }
}
