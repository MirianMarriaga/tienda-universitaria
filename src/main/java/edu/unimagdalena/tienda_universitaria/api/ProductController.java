package edu.unimagdalena.tienda_universitaria.api;


import edu.unimagdalena.tienda_universitaria.api.dto.InventoryDtos.*;
import edu.unimagdalena.tienda_universitaria.api.dto.ProductDtos.*;
import edu.unimagdalena.tienda_universitaria.services.InventoryService;
import edu.unimagdalena.tienda_universitaria.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductCreateRequest req, UriComponentsBuilder uriBuilder) {
        var created = service.create(req);
        var location = uriBuilder.path("/api/products/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody ProductUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @PutMapping("/{id}/inventory")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable Long id, @RequestBody InventoryUpdateRequest req) {
        return ResponseEntity.ok(inventoryService.update(id, req));
    }

}
