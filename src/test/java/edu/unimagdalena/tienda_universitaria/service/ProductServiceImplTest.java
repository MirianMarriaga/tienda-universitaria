package edu.unimagdalena.tienda_universitaria.service;


import edu.unimagdalena.tienda_universitaria.api.dto.ProductDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Category;
import edu.unimagdalena.tienda_universitaria.entities.Product;
import edu.unimagdalena.tienda_universitaria.repositories.CategoryRepository;
import edu.unimagdalena.tienda_universitaria.repositories.ProductRepository;
import edu.unimagdalena.tienda_universitaria.services.ProductServiceImpl;
import edu.unimagdalena.tienda_universitaria.services.mapper.IProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepository productRepo;
    @Mock
    CategoryRepository categoryRepo;
    @Mock
    IProductMapper mapper;

    @InjectMocks
    ProductServiceImpl service;

    @Test
    void shouldCreateProduct() {
        // Given
        var category = Category.builder()
                .id(1L)
                .name("Books")
                .createdAt(Instant.now())
                .build();

        var req = new ProductCreateRequest(
                "BOOK-001", 1L, "Algebra", "Math book", new BigDecimal("45000")
        );

        var product = Product.builder()
                .id(1L)
                .sku("BOOK-001")
                .name("Algebra")
                .category(category)
                .price(new BigDecimal("45000.00"))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        var response = new ProductResponse(
                1L, "BOOK-001", 1L, "Algebra",
                "Math book", new BigDecimal("45000"), true,
                Instant.now(), Instant.now()
        );

        Mockito.when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(productRepo.findBySku("BOOK-001")).thenReturn(Optional.empty());
        when(mapper.toEntity(req)).thenReturn(product);
        when(productRepo.save(any())).thenReturn(product);
        when(mapper.toResponse(product)).thenReturn(response);

        // When
        var result = service.create(req);

        // Then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.sku()).isEqualTo("BOOK-001");
    }

    @Test
    void shouldThrowWhenSkuAlreadyExists() {
        // Given
        var category = Category.builder().id(1L).name("Books").createdAt(Instant.now()).build();
        var req = new ProductCreateRequest("BOOK-001", 1L, "Algebra", "Math book", new BigDecimal("45000"));
        var existing = Product.builder().id(1L).sku("BOOK-001").build();

        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(productRepo.findBySku("BOOK-001")).thenReturn(Optional.of(existing));

        // Then
        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("BOOK-001");
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        when(productRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldDeactivateProduct() {
        // Given
        var product = Product.builder()
                .id(1L)
                .sku("BOOK-001")
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(productRepo.save(any())).thenReturn(product);

        // When
        service.deactivate(1L);

        // Then
        assertThat(product.getActive()).isFalse();
        verify(productRepo).save(product);
    }

    @Test
    void shouldReturnActiveProductsByCategory() {
        // Given
        var product = Product.builder().id(1L).name("Algebra").active(true).build();
        var response = new ProductResponse(1L, "BOOK-001", 1L, "Algebra", "", new BigDecimal("45000"), true, Instant.now(), Instant.now());

        when(productRepo.findByCategory_IdAndActiveTrue(1L)).thenReturn(List.of(product));
        when(mapper.toResponse(product)).thenReturn(response);

        // When
        var result = service.getActiveProductsByCategory(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Algebra");
    }

}
