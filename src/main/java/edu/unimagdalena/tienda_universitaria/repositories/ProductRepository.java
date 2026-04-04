package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Category;
import edu.unimagdalena.tienda_universitaria.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);
    @Query("""
            SELECT p.id, p.name, SUM(io.quantity)
            FROM OrderItem oi
            JOIN oi.product p
            JOIN oi.order o
            WHERE o.createdAt >= :start
            AND o.createdAt <= :end
            GROUP BY p.id, p,name
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<Object[]> findProductsBestSoldByPeriod(
            @Param("start")Instant start,
            @Param("end") Instant end
            );

    @Query("""
            SELECT p.id, p.name, i.availableStock, i.minimumStock
            FROM Product p
            JOIN p.inventory i
            WHERE i.availableStock <= i.minimumStock
            ORDER BY i.availableStock ASC
            """)
    List<Object[]> findByProductsInsufficientStock();
}

