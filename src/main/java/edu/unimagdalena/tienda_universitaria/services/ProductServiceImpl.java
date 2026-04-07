package edu.unimagdalena.tienda_universitaria.services;


import edu.unimagdalena.tienda_universitaria.api.dto.ProductDtos.*;
import edu.unimagdalena.tienda_universitaria.api.dto.ReportDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Product;
import edu.unimagdalena.tienda_universitaria.repositories.CategoryRepository;
import edu.unimagdalena.tienda_universitaria.repositories.ProductRepository;
import edu.unimagdalena.tienda_universitaria.services.mapper.IProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final IProductMapper mapper;

    @Override
    public ProductResponse create(ProductCreateRequest req){
        var category = categoryRepo.findById(req.category())
                .orElseThrow(()-> new RuntimeException("Category %d not found".formatted(req.category())));

        productRepo.findBySku(req.sku()).ifPresent(p ->{throw new RuntimeException("Product with sku %s already exists".formatted(req.sku()));});

        Product product = mapper.toEntity(req);
        product.setCategory(category);
        product.setActive(true);
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        return mapper.toResponse(productRepo.save(product));


    }
    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(Long id){
        return productRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Product  %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> list(){
        return  productRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }
    @Override
    public ProductResponse update(Long id, ProductUpdateRequest req){
        var product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product  %d not found".formatted(id)));
        if ( (req.category() != null)){
            var category = categoryRepo.findById(req.category())
                    .orElseThrow(() -> new RuntimeException("Category  %d not found".formatted(req.category())));
            product.setCategory(category);
        }
        mapper.patch(product, req);
        product.setUpdatedAt(Instant.now());
        return mapper.toResponse(productRepo.save(product));
    }
    @Override
    public void deactivate(Long id) {
        var product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product %d not found".formatted(id)));
        product.setActive(false);
        product.setUpdatedAt(Instant.now());
        productRepo.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductBySku(String sku){
        return productRepo.findBySku(sku)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Product with Sku %s not found".formatted(sku)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProductsByCategory(Long categoryId) {
        return productRepo.findByCategory_IdAndActiveTrue(categoryId).stream()
                .map(mapper::toResponse)
                .toList();
    }

   // @Override
    @Transactional(readOnly = true)
    public List<LowStockProductResponse> getLowStockProducts(){
        return productRepo.findByProductsInsufficientStock().stream()
                .map(row -> new LowStockProductResponse(
                        ((Number) row[0]).longValue(),
                        (String)row[1],
                        ((Number)row[2]).intValue(),
                        ((Number)row[3]).intValue()
                ))
                .toList();
    }

    }
