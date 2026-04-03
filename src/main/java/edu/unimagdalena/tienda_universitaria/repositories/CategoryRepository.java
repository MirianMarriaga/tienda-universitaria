package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameIgnoreCase(String name);
}
