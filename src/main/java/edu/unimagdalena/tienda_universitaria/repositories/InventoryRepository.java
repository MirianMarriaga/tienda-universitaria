package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("""
            SELECT i.product.id, i.product.name, i.availableStock, i.minimumStock
            FROM Inventory i
            WHERE i.availableStock <= i.minimumStock
            ORDER BY i.product.name ASC
            """)
    List<Object[]> findProductsInsufficientStock();
}
