package edu.unimagdalena.tienda_universitaria.services.mapper;

import edu.unimagdalena.tienda_universitaria.api.dto.ProductDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface IProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductCreateRequest req);

    @Mapping(target = "categoryId", source = "category.id")
    ProductResponse toResponse(Product p);

    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(@MappingTarget Product target, ProductUpdateRequest dto);
}
