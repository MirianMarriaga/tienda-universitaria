package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.OrderDtos.*;

import java.util.List;

public interface OrderService {

    OrderResponse created(OrderCreateRequest req);
    OrderResponse get(Long id);
    OrderResponse pay(Long id);
    OrderResponse cancel(Long id);
    OrderResponse ship(Long id);
    OrderResponse deliver(Long id);
    List<OrderResponse> list();

}
