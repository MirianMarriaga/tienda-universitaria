package edu.unimagdalena.tienda_universitaria.services;


import edu.unimagdalena.tienda_universitaria.api.dto.CategoryDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Category;
import edu.unimagdalena.tienda_universitaria.exception.ConflictException;
import edu.unimagdalena.tienda_universitaria.exception.ResourceNotFoundException;
import edu.unimagdalena.tienda_universitaria.exception.ValidationException;
import edu.unimagdalena.tienda_universitaria.repositories.AddressRepository;
import edu.unimagdalena.tienda_universitaria.repositories.CategoryRepository;
import edu.unimagdalena.tienda_universitaria.services.mapper.ICategoryMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final ICategoryMapper mapper;

    @Override
    public CategoryResponse create(CategoryCreateRequest req) {
        if (req.name() == null || req.name().isBlank()) {
            throw new ValidationException("Category name is required");
        }
        categoryRepo.findByNameIgnoreCase(req.name()).ifPresent(c -> {
                    throw new ConflictException("Category %s already exists".formatted(req.name()));
                });

        var category = mapper.toEntity(req);
        category.setCreatedAt(Instant.now());
        var saved = categoryRepo.save(category);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getByName(String name) {

        if (name == null || name.isBlank()) {
            throw new ValidationException("Category name is required");
        }

        var category = categoryRepo.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category %s not found".formatted(name)));

        return mapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse get(Long id) {
        var category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category %d not found".formatted(id)));

        return mapper.toResponse(category);

    }
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> list(){
        return categoryRepo.findAll().stream().map(mapper::toResponse).toList();
    }


}
