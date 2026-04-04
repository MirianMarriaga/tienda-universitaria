package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderStatusHistoryRepository  extends JpaRepository<OrderStatusHistory, Long> {
    @Query("""
            SELECT h
            FROM OrderStatusHistory h
            WHERE h.order.id = :orderId
            ORDER BY h.changedAt ASC
            
            """)
    List<OrderStatusHistory> findHistoryByOrdenId(
            @Param("orderId") Long orderId
    );
}
