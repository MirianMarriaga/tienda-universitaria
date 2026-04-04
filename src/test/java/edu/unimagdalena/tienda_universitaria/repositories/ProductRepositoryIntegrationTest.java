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

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryIntegrationTest extends AbstractRepositoryIT{

    @Autowired
    ProductRepository productRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    InventoryRepository inventoryRepo;

    @Test
    @DisplayName("Product: Busca por categoria")
    void shouldFindByCategory_IdAndActiveTrue(){
        // Given
        var categoria = categoryRepo.save(Category.builder()
                .name("Libro")
                .description("Libros académicos")
                .createdAt(Instant.now())
                .build());

        productRepo.save(Product.builder()
                .sku("LIB-ING-ALG-2ED-045")
                .category(categoria)
                .name("Álgebra para Ingenieria")
                .description("Libro de álgebra")
                .price(BigDecimal.valueOf(45000))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        productRepo.save(Product.builder()
                .sku("LIB-ING-CAL-7ED-005")
                .category(categoria)
                .name("Cálculo Diferencial")
                .description("Libro de cálculo")
                .price(BigDecimal.valueOf(48000))
                .active(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        // When
        List<Product> resultado = productRepo.findByCategory_IdAndActiveTrue(categoria.getId());

        // Then
        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getActive()).isTrue();
        assertThat(resultado.get(0).getCategory().getId())
                .isEqualTo(categoria.getId());
    }

    @Test
    @DisplayName("Product")
    void findByInventory_AvailableStockLessThan(){
        //Given
        var categoria = categoryRepo.save(Category.builder()
                .name("Libro")
                .description("Libros académicos")
                .createdAt(Instant.now())
                .build());

        var producto1 = productRepo.save(Product.builder()
                .sku("LIB-ING-ALG-2ED-045")
                .category(categoria)
                .name("Álgebra para Ingenieria")
                .description("Libro de álgebra")
                .price(BigDecimal.valueOf(45000))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var producto2 = productRepo.save(Product.builder()
                .sku("LIB-ING-CAL-7ED-005")
                .category(categoria)
                .name("Cálculo Diferencial")
                .description("Libro de cálculo")
                .price(BigDecimal.valueOf(48000))
                .active(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var inventory1 = inventoryRepo.save(Inventory.builder()
                .product(producto1)
                .availableStock(3)
                .minimumStock(5)
                .updatedAt(Instant.now())
                .build());

        var inventory2 = inventoryRepo.save(Inventory.builder()
                .product(producto2)
                .availableStock(3)
                .minimumStock(5)
                .updatedAt(Instant.now())
                .build());
        //When
        List<Product> resultado = productRepo.findByInventory_AvailableStockLessThan(5); //minimumStock

        //Then
        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(2);
    }
}
