package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.ProductDtos.*;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductCreateRequest req);
    ProductResponse get(Long id);
    ProductResponse update(Long id, ProductUpdateRequest req);
    List<ProductResponse> list();

}
