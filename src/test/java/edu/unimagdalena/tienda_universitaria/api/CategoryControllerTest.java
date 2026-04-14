package edu.unimagdalena.tienda_universitaria.api;


import edu.unimagdalena.tienda_universitaria.api.dto.CategoryDtos.*;
import edu.unimagdalena.tienda_universitaria.exception.ConflictException;
import edu.unimagdalena.tienda_universitaria.exception.ResourceNotFoundException;
import edu.unimagdalena.tienda_universitaria.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import tools.jackson.databind.ObjectMapper;


import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    CategoryService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new CategoryCreateRequest("Books", "University books");
        var resp = new CategoryResponse(1L, "Books", "University books", Instant.now());

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @Test
    void create_shouldReturn409WhenCategoryAlreadyExists() throws Exception {
        var req = new CategoryCreateRequest("Books", "University books");

        when(service.create(any()))
                .thenThrow(new ConflictException("Category Books already exists"));

        mvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void get_shouldReturn200() throws Exception {
        var resp = new CategoryResponse(1L, "Books", "University books", Instant.now());

        when(service.get(1L)).thenReturn(resp);

        mvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Books"));
    }

    @Test
    void get_shouldReturn404WhenNotFound() throws Exception {
        when(service.get(99L))
                .thenThrow(new ResourceNotFoundException("Category 99 not found"));

        mvc.perform(get("/api/categories/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void list_shouldReturn200() throws Exception {
        var resp = new CategoryResponse(1L, "Books", "University books", Instant.now());

        when(service.list()).thenReturn(List.of(resp));

        mvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Books"));
    }

}
