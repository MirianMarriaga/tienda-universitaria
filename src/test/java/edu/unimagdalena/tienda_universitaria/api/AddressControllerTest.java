package edu.unimagdalena.tienda_universitaria.api;


import edu.unimagdalena.tienda_universitaria.api.dto.AddressDtos.*;
import edu.unimagdalena.tienda_universitaria.exception.ResourceNotFoundException;
import edu.unimagdalena.tienda_universitaria.services.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AddressController.class)
public class AddressControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    AddressService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new AddressCreateRequest(1L, "Manzana G, casa 6", "Santa Marta", "Magdalena", "Colombia");
        var resp = new AddressResponse(1L, 1L, "Manzana G, casa 6", "Santa Marta", "Magdalena", "Colombia", Instant.now());

        when(service.create(eq(1L), any())).thenReturn(resp);

        mvc.perform(post("/api/customers/1/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.city").value("Santa Marta"));
    }

    @Test
    void create_shouldReturn404WhenCustomerNotFound() throws Exception {
        var req = new AddressCreateRequest(99L, "Street 22", "Santa Marta", "Magdalena", "Colombia");

        when(service.create(eq(99L), any()))
                .thenThrow(new ResourceNotFoundException("Customer 99 not found"));

        mvc.perform(post("/api/customers/99/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void listByCustomer_shouldReturn200() throws Exception {
        var resp = new AddressResponse(1L, 1L, "Street 22", "Santa Marta", "Magdalena", "Colombia", Instant.now());

        when(service.listByCustomer(1L)).thenReturn(List.of(resp));

        mvc.perform(get("/api/customers/1/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Santa Marta"));
    }

}
