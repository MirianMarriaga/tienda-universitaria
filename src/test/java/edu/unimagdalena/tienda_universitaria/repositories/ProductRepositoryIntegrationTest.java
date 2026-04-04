package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Category;
import edu.unimagdalena.tienda_universitaria.entities.Inventory;
import edu.unimagdalena.tienda_universitaria.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryIntegrationTest extends AbstractRepositoryIT{

    @Autowired
    ProductRepository productRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    InventoryRepository inventoryRepo;

    @Test
    @DisplayName("Product: Search by SKU")
    void shouldFindBySku(){
        // Given
        var category = categoryRepo.save(Category.builder()
                .name("Books")
                .description("Academic books")
                .createdAt(Instant.now())
                .build());

        productRepo.save(Product.builder()
                .sku("BOOK-ENG-ALG-2ED-045")
                .category(category)
                .name("Engineering Algebra")
                .description("Algebra book")
                .price(BigDecimal.valueOf(45000))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        productRepo.save(Product.builder()
                .sku("BOOK-ENG-CAL-7ED-005")
                .category(category)
                .name("Differential Calculus")
                .description("Calculus book")
                .price(BigDecimal.valueOf(48000))
                .active(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        //When
        Optional<Product> result = productRepo.findBySku("BOOK-ENG-ALG-2ED-045");

        //Then
        assertThat(result).isPresent();
        assertThat(result.get().getSku()).isEqualTo("BOOK-ENG-ALG-2ED-045");
    }

    @Test
    @DisplayName("Product: Search by category")
    void shouldFindByCategory_IdAndActiveTrue(){
        // Given
        var category = categoryRepo.save(Category.builder()
                .name("Books")
                .description("Academic books")
                .createdAt(Instant.now())
                .build());

        productRepo.save(Product.builder()
                .sku("BOOK-ENG-ALG-2ED-045")
                .category(category)
                .name("Engineering Algebra")
                .description("Algebra book")
                .price(BigDecimal.valueOf(45000))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        productRepo.save(Product.builder()
                .sku("BOOK-ENG-CAL-7ED-005")
                .category(category)
                .name("Differential Calculus")
                .description("Calculus book")
                .price(BigDecimal.valueOf(48000))
                .active(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        // When
        List<Product> result = productRepo.findByCategory_IdAndActiveTrue(category.getId());

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActive()).isTrue();
        assertThat(result.get(0).getCategory().getId())
                .isEqualTo(category.getId());
    }

    @Test
    @DisplayName("Product: Search by low stock")
    void findByInventory_AvailableStockLessThan(){
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
                .price(BigDecimal.valueOf(45000))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var product2 = productRepo.save(Product.builder()
                .sku("BOOK-ENG-CAL-7ED-005")
                .category(category)
                .name("Differential Calculus")
                .description("Calculus book")
                .price(BigDecimal.valueOf(48000))
                .active(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());


        var inventory1 = inventoryRepo.save(Inventory.builder()
                .product(product1)
                .availableStock(3)
                .minimumStock(5)
                .updatedAt(Instant.now())
                .build());

        var inventory2 = inventoryRepo.save(Inventory.builder()
                .product(product2)
                .availableStock(3)
                .minimumStock(5)
                .updatedAt(Instant.now())
                .build());

        // When
        List<Product> result = productRepo.findByInventory_AvailableStockLessThan(5);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
    }
}
