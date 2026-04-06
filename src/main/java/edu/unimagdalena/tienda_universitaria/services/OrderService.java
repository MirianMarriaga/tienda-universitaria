package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.OrderDtos.*;
import edu.unimagdalena.tienda_universitaria.api.dto.ReportDtos;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface OrderService {

    OrderResponse create(OrderCreateRequest req);
    OrderResponse get(Long id);
    OrderResponse pay(Long id);
    OrderResponse cancel(Long id);
    OrderResponse ship(Long id);
    OrderResponse deliver(Long id);
    List<OrderResponse> list();

    List<OrderResponse> findByCustomerId(Long customerId);
    List<OrderResponse> findByStatus(OrderStatus status);
    List<OrderResponse> findByFilters(
            Long customerId,
            OrderStatus status,
            Instant startDate,
            Instant endDate,
            BigDecimal minTotal,
            BigDecimal maxTotal);
}
