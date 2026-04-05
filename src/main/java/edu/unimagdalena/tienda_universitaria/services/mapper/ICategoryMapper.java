package edu.unimagdalena.tienda_universitaria.services.mapper;

import edu.unimagdalena.tienda_universitaria.api.dto.CategoryDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Category toEntity(CategoryCreateRequest req);

    CategoryResponse toResponse(Category c);
}
