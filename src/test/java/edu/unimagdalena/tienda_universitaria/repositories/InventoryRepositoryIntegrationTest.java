package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Category;
import edu.unimagdalena.tienda_universitaria.entities.Inventory;
import edu.unimagdalena.tienda_universitaria.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryRepositoryIntegrationTest extends AbstractRepositoryIT{

    @Autowired
    ProductRepository productRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    InventoryRepository inventoryRepo;

    @Test
    @DisplayName("Inventory: Search by product id")
    void shouldFindByProduct_Id(){
        //Given
        var category = categoryRepo.save(Category.builder()
                .name("Books")
                .description("Academic books")
                .createdAt(Instant.now())
                .build());

        var product1 = productRepo.save(Product.builder()
                .sku("BOOK-ENG-ALG-2ED-045")
                .category(category)
                .name("Engineering Algebra")
                .description("Algebra book")
                .price(BigDecimal.valueOf(45000.00))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        var product2 = productRepo.save(Product.builder()
                .sku("BOOK-ENG-CAL-7ED-005")
                .category(category)
                .name("Differential Calculus")
                .description("Calculus book")
                .price(BigDecimal.valueOf(48000.00))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        inventoryRepo.save(Inventory.builder()
                .product(product1)
                .availableStock(15)
                .minimumStock(5)
                .updatedAt(Instant.now())
                .build());

        //When
        Optional<Inventory> result = inventoryRepo.findByProduct_Id(product1.getId());

        //Then
        assertThat(result).isPresent();
        assertThat(result.get().getProduct().getId()).isEqualTo(product1.getId());
    }
}
