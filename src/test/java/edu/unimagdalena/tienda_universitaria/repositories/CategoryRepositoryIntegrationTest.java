package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRepositoryIntegrationTest extends AbstractRepositoryIT{
    @Autowired
    CategoryRepository categoryRepo;

    @Test
    @DisplayName("Category: Search by name")
    void shouldFindByNameIgnoreCase(){
        //Given
        var category1 = categoryRepo.save(Category.builder()
                .name("Books")
                .description("Academic books")
                .createdAt(Instant.now())
                .build());

        categoryRepo.save(Category.builder()
                .name("Technology")
                .description("Devices, software, and tech-related products")
                .createdAt(Instant.now())
                .build());

        //When
        Optional<Category> result = categoryRepo.findByNameIgnoreCase("BOOKS");

        //Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(category1.getId());
    }
}
