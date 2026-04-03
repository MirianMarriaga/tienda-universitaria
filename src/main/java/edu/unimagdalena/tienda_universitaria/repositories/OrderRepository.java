package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> findByCustomerId(Long customerId);
}
