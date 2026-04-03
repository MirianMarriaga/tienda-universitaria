package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
    List<OrderStatusHistory> findByOrder_IdOrderByChangedAtAsc(Long orderId);
}
