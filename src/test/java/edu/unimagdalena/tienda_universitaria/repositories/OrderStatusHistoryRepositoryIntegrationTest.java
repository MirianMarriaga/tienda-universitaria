package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Address;
import edu.unimagdalena.tienda_universitaria.entities.Customer;
import edu.unimagdalena.tienda_universitaria.entities.Order;
import edu.unimagdalena.tienda_universitaria.entities.OrderStatusHistory;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusHistoryRepositoryIntegrationTest extends AbstractRepositoryIT {

    @Autowired
    OrderStatusHistoryRepository orderStatusHistoryRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    AddressRepository addressRepo;

    @Test
    @DisplayName("OrderStatusHistory: Search history by ordenId")
    void findHistoryByOrdenId(){
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

        var address = addressRepo.save(Address.builder()
                .customer(customer1)
                .street("Mz G casa 6 Barrio Santa Cruz")
                .city("Santa Marta")
                .state("Magdalena")
                .country("Colombia")
                .createdAt(Instant.now())
                .build());

        var order = orderRepo.save(Order.builder()
                .customer(customer1)
                .address(address)
                .status(OrderStatus.SHIPPED)
                .total(new BigDecimal("300000.00"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        orderStatusHistoryRepo.save(OrderStatusHistory.builder()
                .order(order)
                .status(OrderStatus.CREATED)
                .changedAt(Instant.now().minusSeconds(300))
                .build());
        orderStatusHistoryRepo.save(OrderStatusHistory.builder()
                .order(order)
                .status(OrderStatus.PAID)
                .changedAt(Instant.now().minusSeconds(200))
                .build());
        orderStatusHistoryRepo.save(OrderStatusHistory.builder()
                .order(order)
                .status(OrderStatus.SHIPPED)
                .changedAt(Instant.now().minusSeconds(100))
                .build());

        //When
        List<OrderStatusHistory> result = orderStatusHistoryRepo.findHistoryByOrdenId(order.getId());

        //Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(result.get(1).getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(result.get(2).getStatus()).isEqualTo(OrderStatus.SHIPPED);



    }

}
