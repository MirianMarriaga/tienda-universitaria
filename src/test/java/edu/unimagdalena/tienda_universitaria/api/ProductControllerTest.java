package edu.unimagdalena.tienda_universitaria.api;




import edu.unimagdalena.tienda_universitaria.api.dto.InventoryDtos.*;
import edu.unimagdalena.tienda_universitaria.api.dto.ProductDtos.*;
import edu.unimagdalena.tienda_universitaria.exception.ConflictException;
import edu.unimagdalena.tienda_universitaria.exception.ResourceNotFoundException;
import edu.unimagdalena.tienda_universitaria.services.InventoryService;
import edu.unimagdalena.tienda_universitaria.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    ProductService service;

    @MockitoBean
    InventoryService inventoryService;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new ProductCreateRequest("BOOK-001", 1L, "Algebra", "Baldor Algebra Book", new BigDecimal("45000.00"));
        var resp = new ProductResponse(1L, "BOOK-001", 1L, "Algebra", "Baldor Algebra Book", new BigDecimal("45000.00"), true, Instant.now(), Instant.now());

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sku").value("BOOK-001"));
    }

    @Test
    void create_shouldReturn409WhenSkuAlreadyExists() throws Exception {
        var req = new ProductCreateRequest("BOOK-001", 1L, "Algebra", "Baldor Algebra Book", new BigDecimal("45000.00"));

        when(service.create(any()))
                .thenThrow(new ConflictException("SKU BOOK-001 already exists"));

        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void get_shouldReturn200() throws Exception {
        var resp = new ProductResponse(1L, "BOOK-001", 1L, "Algebra", "Baldor Algebra Book", new BigDecimal("45000.00"), true, Instant.now(), Instant.now());

        when(service.get(1L)).thenReturn(resp);

        mvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sku").value("BOOK-001"));
    }

    @Test
    void get_shouldReturn404WhenNotFound() throws Exception {
        when(service.get(99L))
                .thenThrow(new ResourceNotFoundException("Product 99 not found"));

        mvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void list_shouldReturn200() throws Exception {
        var resp = new ProductResponse(1L, "BOOK-001", 1L, "Algebra", "Baldor Algebra Book", new BigDecimal("45000.00"), true, Instant.now(), Instant.now());

        when(service.list()).thenReturn(List.of(resp));

        mvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("BOOK-001"));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var req = new ProductUpdateRequest(1L, "Algebra Updated", "Updated description", new BigDecimal("50000.00"), true);
        var resp = new ProductResponse(1L, "BOOK-001", 1L, "Algebra Updated", "Updated description", new BigDecimal("50000.00"), true, Instant.now(), Instant.now());

        when(service.update(eq(1L), any())).thenReturn(resp);

        mvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Algebra Updated"));
    }

    @Test
    void updateInventory_shouldReturn200() throws Exception {
        var req = new InventoryUpdateRequest(50, 10);
        var resp = new InventoryResponse(1L, 1L, 50, 10, Instant.now());

        when(inventoryService.update(eq(1L), any())).thenReturn(resp);

        mvc.perform(put("/api/products/1/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableStock").value(50));
    }

}
