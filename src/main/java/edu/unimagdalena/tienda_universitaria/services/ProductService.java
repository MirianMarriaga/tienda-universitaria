package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.ProductDtos.*;
import edu.unimagdalena.tienda_universitaria.api.dto.ReportDtos.*;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductCreateRequest req);
    ProductResponse get(Long id);
    List<ProductResponse> list();
    ProductResponse update(Long id, ProductUpdateRequest req);
    void deactivate(Long id);
    ProductResponse getProductBySku(String sku);
    List<ProductResponse> getActiveProductsByCategory(Long categoryId);

}
