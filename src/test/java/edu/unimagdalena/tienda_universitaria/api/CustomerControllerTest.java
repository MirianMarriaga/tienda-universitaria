package edu.unimagdalena.tienda_universitaria.api;

import edu.unimagdalena.tienda_universitaria.api.dto.CustomerDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import edu.unimagdalena.tienda_universitaria.exception.ResourceNotFoundException;
import edu.unimagdalena.tienda_universitaria.services.CustomerService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean
    CustomerService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new CustomerCreateRequest("1001234567", "Juan Amador Hernandez",
                "jahernandez@unimagdalena.edu.co", "+57 310 456 7821", CustomerStatus.ACTIVE);

        var resp = new CustomerResponse(10L, "1001234567", "Juan Amador Hernandez",
                "jahernandez@unimagdalena.edu.co", "+57 310 456 7821", CustomerStatus.ACTIVE,
                Instant.now(), Instant.now());

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("api/customers/10")))
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void create_shouldReturn400WhenInvalidBody() throws Exception {

        var req = new CustomerCreateRequest(
                "1001234567", "Juan Amador Hernandez",
                "jahernandez@unimagdalena.edu.co", "+57 310 456 7821", null);

        mvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void get_shouldReturn200() throws Exception {
        when(service.get(5L)).thenReturn(new CustomerResponse(5L, "Juan Amador Hernandez",
                "1001234567", "jahernandez@unimagdalena.edu.co",
                "+57 310 456 7821", CustomerStatus.ACTIVE, Instant.now(), Instant.now()));

        mvc.perform(get("/api/customers/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }


    @Test
    void get_shouldReturn404WhenNotFound() throws Exception {
        when(service.get(99L)).thenThrow(new ResourceNotFoundException("customer 99 not found"));

        mvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("customer 99 not found"));
    }

    @Test
    void update_shouldReturn204() throws Exception {

        var req = new CustomerUpdateRequest(
                "Juan Amador Hernandez",
                "jahernandez@unimagdalena.edu.co",
                "+57 310 456 7821",
                CustomerStatus.ACTIVE
        );

        mvc.perform(put("/api/customers/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNoContent());

        verify(service).update(eq(3L), any(CustomerUpdateRequest.class));
    }
}
