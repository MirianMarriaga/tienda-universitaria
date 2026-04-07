package edu.unimagdalena.tienda_universitaria.service;

import edu.unimagdalena.tienda_universitaria.api.dto.CategoryDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Category;
import edu.unimagdalena.tienda_universitaria.repositories.CategoryRepository;
import edu.unimagdalena.tienda_universitaria.services.CategoryServiceImpl;
import edu.unimagdalena.tienda_universitaria.services.mapper.ICategoryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock CategoryRepository categoryRepo;
    @Mock ICategoryMapper mapper;

    @InjectMocks
    CategoryServiceImpl service;

    @Test
    void shouldCreateCategory() {
        // Given
        var req = new CategoryCreateRequest("Books", "University books");
        var category = Category.builder().id(1L).name("Books").description("University books").createdAt(Instant.now()).build();
        var response = new CategoryResponse(1L, "Books", "University books", Instant.now());

        when(mapper.toEntity(req)).thenReturn(category);
        when(categoryRepo.save(any())).thenReturn(category);
        when(mapper.toResponse(category)).thenReturn(response);

        // When
        var result = service.create(req);

        // Then
        assertThat(result.name()).isEqualTo("Books");
    }

    @Test
    void shouldGetCategoryById() {
        // Given
        var category = Category.builder().id(1L).name("Books").build();
        var response = new CategoryResponse(1L, "Books", "University books", Instant.now());

        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.toResponse(category)).thenReturn(response);

        // When
        var result = service.get(1L);

        // Then
        assertThat(result.name()).isEqualTo("Books");
    }

    @Test
    void shouldThrowWhenCategoryNotFound() {
        // Given
        when(categoryRepo.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldGetCategoryByName() {
        // Given
        var category = Category.builder().id(1L).name("Books").build();
        var response = new CategoryResponse(1L, "Books", "University books", Instant.now());

        when(categoryRepo.findByNameIgnoreCase("Books")).thenReturn(Optional.of(category));
        when(mapper.toResponse(category)).thenReturn(response);

        // When
        var result = service.getByName("Books");

        // Then
        assertThat(result.name()).isEqualTo("Books");
    }

    @Test
    void shouldThrowWhenCategoryNotFoundByName() {
        // Given
        when(categoryRepo.findByNameIgnoreCase("Books")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> service.getByName("Books"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Books");
    }

    @Test
    void shouldListAllCategories() {
        // Given
        var category = Category.builder().id(1L).name("Books").build();
        var response = new CategoryResponse(1L, "Books", "University books", Instant.now());

        when(categoryRepo.findAll()).thenReturn(List.of(category));
        when(mapper.toResponse(category)).thenReturn(response);

        // When
        var result = service.list();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Books");
    }
}