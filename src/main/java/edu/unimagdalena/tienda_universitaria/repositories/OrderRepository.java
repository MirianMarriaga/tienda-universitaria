package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Order;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer_Id(Long customerId);

    List<Order> findByStatus(OrderStatus orderStatus);
}
