package edu.unimagdalena.tienda_universitaria.services;


import edu.unimagdalena.tienda_universitaria.api.dto.CategoryDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Category;
import edu.unimagdalena.tienda_universitaria.exception.ResourceNotFoundException;
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
        var category = mapper.toEntity(req);
        category.setCreatedAt(Instant.now());
        var saved = categoryRepo.save(category);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getByName(String name) {
        return categoryRepo.findByNameIgnoreCase(name)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category %s not found".formatted(name)));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse get(Long id) {
        return categoryRepo.findById(id).map(mapper::toResponse).orElseThrow(() -> new ResourceNotFoundException("Category %d not found".formatted(id)));
    }
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> list(){
        return categoryRepo.findAll().stream().map(mapper::toResponse).toList();
    }


}
