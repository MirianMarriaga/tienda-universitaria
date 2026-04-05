package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.AddressDtos.*;
import edu.unimagdalena.tienda_universitaria.api.dto.CategoryDtos.*;
import edu.unimagdalena.tienda_universitaria.repositories.CategoryRepository;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryCreateRequest req);
    CategoryResponse get(Long id);
    List<CategoryResponse> list();
    CategoryRepository findByName(String name);

}
