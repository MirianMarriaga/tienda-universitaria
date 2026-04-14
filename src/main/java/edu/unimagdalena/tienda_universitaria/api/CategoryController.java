package edu.unimagdalena.tienda_universitaria.api;

import edu.unimagdalena.tienda_universitaria.api.dto.CategoryDtos.*;
import edu.unimagdalena.tienda_universitaria.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest req, UriComponentsBuilder uriBuilder) {
        var created = service.create(req);
        var location = uriBuilder.path("/api/categories/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

}
