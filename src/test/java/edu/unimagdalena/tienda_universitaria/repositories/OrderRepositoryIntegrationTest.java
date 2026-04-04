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

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryIntegrationTest extends AbstractRepositoryIT {
    @Autowired
    OrderRepository orderRepo;
    @Autowired
    CustomerRepository customerRepo;
    @Autowired
    OrderStatusHistoryRepository orderStatusHistoryRepo;
    @Autowired
    AddressRepository addressRepo;


    @Test
    @DisplayName("Order: Search by status")
    void shouldFindByStatus() {
        //Given
        var customer = customerRepo.save(Customer.builder()
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .email("jahernandez@unimagdalena.edu.co")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var address = addressRepo.save(Address.builder()
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
                .total(BigDecimal.valueOf(48000.00))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var order2 = orderRepo.save(Order.builder()
                .customer(customer)
                .address(address)
                .status(OrderStatus.DELIVERED)
                .total(BigDecimal.valueOf(45000.00))
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
    @DisplayName("Order: Search by customerId")
    void findByCustomer_Id() {
        //Given
        var customer1 = customerRepo.save(Customer.builder()
                .fullName("Angelica Villegas")
                .email("angelicamvg@gmail.com")
                .identificationNumber("1001234567")
                .phone("+57 311 254 3939")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var customer2 = customerRepo.save(Customer.builder()
                .fullName("Priscila Duran")
                .email("priscilabf@gmail.com")
                .identificationNumber("1008327389")
                .phone("+57 312 567 4899")
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

        orderRepo.save(Order.builder()
                .customer(customer1)
                .address(address1)
                .status(OrderStatus.CREATED)
                .total(new BigDecimal("180000"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        orderRepo.save(Order.builder()
                .customer(customer2)
                .address(address2)
                .status(OrderStatus.CREATED)
                .total(new BigDecimal("31000"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        //When
        List<Order> result = orderRepo.findByCustomer_Id(customer1.getId());
        //Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomer().getId()).isEqualTo(customer1.getId());
    }

    @Test
    @DisplayName("Order: Search order by filters")
    void findOrdersByFilters() {
        //Given
        var customer1 = customerRepo.save(Customer.builder()
                .fullName("Angelica Villegas")
                .email("angelicamvg@gmail.com")
                .identificationNumber("1001234567")
                .phone("+57 311 254 3939")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var customer2 = customerRepo.save(Customer.builder()
                .fullName("Priscila Duran")
                .email("priscilabf@gmail.com")
                .identificationNumber("1008327389")
                .phone("+57 312 567 4899")
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

        orderRepo.save(Order.builder()
                .customer(customer1)
                .address(address1)
                .status(OrderStatus.PAID)
                .total(new BigDecimal("180000.00"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        orderRepo.save(Order.builder()
                .customer(customer2)
                .address(address2)
                .status(OrderStatus.CREATED)
                .total(new BigDecimal("31000.00"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        //When
        List<Order> result = orderRepo.findOrdersByFilters(
                null,
                OrderStatus.PAID,
                null,
                null,
                null,
                null
        );
        //Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("Order: Search by monthly revenue")
    void findByMonthlyRevenue() {
        //Given
        var customer1 = customerRepo.save(Customer.builder()
                .fullName("Angelica Villegas")
                .email("angelicamvg@gmail.com")
                .identificationNumber("1001234567")
                .phone("+57 311 254 3939")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var customer2 = customerRepo.save(Customer.builder()
                .fullName("Priscila Duran")
                .email("priscilabf@gmail.com")
                .identificationNumber("1008327389")
                .phone("+57 312 567 4899")
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

        orderRepo.save(Order.builder()
                .customer(customer1)
                .address(address1)
                .status(OrderStatus.PAID)
                .total(new BigDecimal("180000.00"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        orderRepo.save(Order.builder()
                .customer(customer2)
                .address(address2)
                .status(OrderStatus.PAID)
                .total(new BigDecimal("31000.00"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        orderRepo.save(Order.builder()
                .customer(customer1)
                .address(address1)
                .status(OrderStatus.CANCELLED)
                .total(new BigDecimal("400000.00"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        //When
        List<Object[]> result = orderRepo.findByMonthlyRevenue();
        //Then
        assertThat(result).hasSize(1);
        BigDecimal monthTotal = (BigDecimal) result.get(0)[2];
        assertThat(monthTotal).isEqualByComparingTo(new BigDecimal("211000.00"));

    }
}