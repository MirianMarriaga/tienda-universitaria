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

class OrderRepositoryIntegrationTest extends AbstractRepositoryIT{
    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    AddressRepository addressRepo;

    @Autowired
    OrderRepository orderRepo;

    @Test
    @DisplayName("Order: Search by status")
    void shouldFindByStatus(){
        //Given
        var customer= customerRepo.save(Customer.builder()
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .email("jahernandez@unimagdalena.edu.co")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var address= addressRepo.save(Address.builder()
                .customer(customer)
                .street("Calle 22 #5-30")
                .city("Santa Marta")
                .state("Magdalena")
                .country("Colombia")
                .createdAt(Instant.now())
                .build());

        var order1 = orderRepo.save(Order.builder()
                        .customer(customer)
                        .address(address)
                        .status(OrderStatus.SHIPPED)
                        .total(BigDecimal.valueOf(48000))
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build());

        var order2 = orderRepo.save(Order.builder()
                .customer(customer)
                .address(address)
                .status(OrderStatus.DELIVERED)
                .total(BigDecimal.valueOf(45000))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        //When
        List<Order> result = orderRepo.findByStatus(OrderStatus.DELIVERED);

        //Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.DELIVERED);
        assertThat(result.get(0).getId()).isEqualTo(order2.getId());
    }

    @Test
    @DisplayName("Order: Search by customer Id")
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

        var order1 = orderRepo.save(Order.builder()
                .customer(customer1)
                .address(address1)
                .status(OrderStatus.SHIPPED)
                .total(BigDecimal.valueOf(48000))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var order2 = orderRepo.save(Order.builder()
                .customer(customer2)
                .address(address2)
                .status(OrderStatus.DELIVERED)
                .total(BigDecimal.valueOf(45000))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        //When
        List<Order> result = orderRepo.findByCustomer_Id(customer2.getId());

        //Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCustomer().getId()).isEqualTo(customer2.getId());
        assertThat(result.get(0).getId()).isEqualTo(order2.getId());
    }

}
