package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCase(String name);

    @Query("""
            SELECT p.category.id, p.category.name, SUM(oi.quantity)
            FROM OrderItem oi
            JOIN oi.product p
            GROUP BY p.category.id, p.category.name
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<Object[]> findTopByCategory();
}