package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Order;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerIdOrder(Long customerId);
    @Query("""
            SELECT o 
            FROM Order o
            WHERE (:customerId IS NULL OR o.customer.id = :customerId)
                AND (:status IS NULL OR o.status = :status)
                AND (:StartDate IS NULL OR o.createdAt >= :StartDate)
                AND (:EndDate IS NULL OR o.createdAt <= :EndDate)
                AND (:MinTotal IS NULL OR o.total >= :MinTotal)
                AND (:MaxTotal IS NULL OR o.total <= :MaxTotal)    
            ORDER BY o.createdAt DESC              
            """)
    List<Order> findOrdersByFilters(
            @Param("customerId") Long customerId,
            @Param("status") OrderStatus status,
            @Param("StartDate") Instant StartDate,
            @Param("EndDate")Instant EndDate,
            @Param("MinTotal") BigDecimal MinTotal,
            @Param("MaxTotal") BigDecimal MaxTotal
    );

    @Query("""
            SELECT EXTRACT(YEAR FROM o.createdAt),
                EXTRACT(MONTH FROM o.createdAt),
                SUM(o.total)
            FROM Order o
            WHERE o.status <> 'CANCELLED'
            GROUP BY EXTRACT(YEAR FROM o.createdAt),
                EXTRACT(MONTH FROM o.createdAt)
            ORDER BY EXTRACT(YEAR FROM o.createdAt),
                EXTRACT(MONTH FROM o.createdAt)                    
            """)
    List<Object[]> findByMonthlyRevenue();
}
