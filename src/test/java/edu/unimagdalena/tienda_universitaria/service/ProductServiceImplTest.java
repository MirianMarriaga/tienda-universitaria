package edu.unimagdalena.tienda_universitaria.service;

import edu.unimagdalena.tienda_universitaria.api.dto.ProductDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Category;
import edu.unimagdalena.tienda_universitaria.entities.Product;
import edu.unimagdalena.tienda_universitaria.exception.ConflictException;
import edu.unimagdalena.tienda_universitaria.exception.ResourceNotFoundException;
import edu.unimagdalena.tienda_universitaria.exception.ValidationException;
import edu.unimagdalena.tienda_universitaria.repositories.CategoryRepository;
import edu.unimagdalena.tienda_universitaria.repositories.OrderRepository;
import edu.unimagdalena.tienda_universitaria.repositories.ProductRepository;
import edu.unimagdalena.tienda_universitaria.services.ProductServiceImpl;
import edu.unimagdalena.tienda_universitaria.services.mapper.IProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock ProductRepository productRepo;
    @Mock CategoryRepository categoryRepo;
    @Mock IProductMapper mapper;
    @Mock OrderRepository orderRepo;

    @InjectMocks
    ProductServiceImpl service;

    @Test
    void shouldCreateProduct() {
        // Given
        var category = Category.builder().id(1L).name("Books").createdAt(Instant.now()).build();
        var req = new ProductCreateRequest("BOOK-001", 1L, "Algebra", "Math book", new BigDecimal("45000.00"));

        var product = Product.builder()
                .id(1L).sku("BOOK-001").name("Algebra")
                .category(category).price(new BigDecimal("45000.00"))
                .active(true).createdAt(Instant.now()).updatedAt(Instant.now())
                .build();

        var response = new ProductResponse(1L, "BOOK-001", 1L, "Algebra", "Math book",
                new BigDecimal("45000.00"), true, Instant.now(), Instant.now());

        when(productRepo.findBySku("BOOK-001")).thenReturn(Optional.empty());
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
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
    void shouldThrowWhenPriceIsZeroOrNegative() {
        // Given
        var req = new ProductCreateRequest("BOOK-001", 1L, "Algebra", "Math book", new BigDecimal("0"));

        // When / Then
        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("greater than zero");
    }

    @Test
    void shouldThrowWhenPriceIsNull() {
        // Given
        var req = new ProductCreateRequest("BOOK-001", 1L, "Algebra", "Math book", null);

        // When / Then
        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Price");
    }

    @Test
    void shouldThrowWhenSkuAlreadyExists() {
        // Given
        var req = new ProductCreateRequest("BOOK-001", 1L, "Algebra", "Math book", new BigDecimal("45000.00"));
        var existing = Product.builder().id(1L).sku("BOOK-001").build();

        when(productRepo.findBySku("BOOK-001")).thenReturn(Optional.of(existing));

        // When / Then
        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("BOOK-001");
    }

    @Test
    void shouldThrowWhenCategoryNotFoundOnCreate() {
        // Given
        var req = new ProductCreateRequest("BOOK-001", 99L, "Algebra", "Math book", new BigDecimal("45000.00"));

        when(productRepo.findBySku("BOOK-001")).thenReturn(Optional.empty());
        when(categoryRepo.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldGetProduct() {
        // Given
        var product = Product.builder().id(1L).name("Algebra").build();
        var response = new ProductResponse(1L, "BOOK-001", 1L, "Algebra", "", new BigDecimal("45000.00"), true, Instant.now(), Instant.now());

        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(mapper.toResponse(product)).thenReturn(response);

        // When
        var result = service.get(1L);

        // Then
        assertThat(result.name()).isEqualTo("Algebra");
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        // Given
        when(productRepo.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldDeactivateProduct() {
        // Given
        var product = Product.builder()
                .id(1L).sku("BOOK-001").active(true)
                .createdAt(Instant.now()).updatedAt(Instant.now())
                .build();

        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(productRepo.save(any())).thenReturn(product);
        when(orderRepo.findAll()).thenReturn(List.of());

        // When
        service.deactivate(1L);

        // Then
        assertThat(product.getActive()).isFalse();
        verify(productRepo).save(product);
    }

    @Test
    void shouldReturnActiveProductsByCategory() {
        // Given
        var category = Category.builder().id(1L).build();
        var product = Product.builder().id(1L).name("Algebra").active(true).build();
        var response = new ProductResponse(1L, "BOOK-001", 1L, "Algebra", "", new BigDecimal("45000.00"), true, Instant.now(), Instant.now());

        when(categoryRepo.existsById(1L)).thenReturn(true);
        when(productRepo.findByCategory_IdAndActiveTrue(1L)).thenReturn(List.of(product));
        when(mapper.toResponse(product)).thenReturn(response);

        // When
        var result = service.getActiveProductsByCategory(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Algebra");
    }

    @Test
    void shouldUpdateProduct() {
        // Given
        var category = Category.builder().id(2L).name("Science").build();
        var product = Product.builder().id(1L).name("Algebra").active(true).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var req = new ProductUpdateRequest(2L, "Algebra II", "Updated", new BigDecimal("50000.00"), true);
        var response = new ProductResponse(1L, "BOOK-001", 2L, "Algebra II", "Updated", new BigDecimal("50000.00"), true, Instant.now(), Instant.now());

        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepo.findById(2L)).thenReturn(Optional.of(category));
        when(productRepo.save(any())).thenReturn(product);
        when(mapper.toResponse(product)).thenReturn(response);

        // When
        var result = service.update(1L, req);

        // Then
        assertThat(result.name()).isEqualTo("Algebra II");
        verify(productRepo).save(product);
    }
}