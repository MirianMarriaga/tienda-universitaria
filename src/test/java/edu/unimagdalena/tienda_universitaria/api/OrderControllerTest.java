package edu.unimagdalena.tienda_universitaria.api;

import edu.unimagdalena.tienda_universitaria.api.dto.OrderDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import edu.unimagdalena.tienda_universitaria.exception.BusinessException;
import edu.unimagdalena.tienda_universitaria.exception.ResourceNotFoundException;
import edu.unimagdalena.tienda_universitaria.exception.ValidationException;
import edu.unimagdalena.tienda_universitaria.services.OrderService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean OrderService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new OrderCreateRequest(1L, 1L,
                List.of(new OrderCreateItemRequest(1L, 2)));

        var resp = new OrderResponse(10L, 1L, 1L, OrderStatus.CREATED,
                BigDecimal.valueOf(30000), List.of(), Instant.now(), Instant.now());

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("api/orders/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void create_shouldReturn400WhenNoItems() throws Exception {
        var req = new OrderCreateRequest(1L, 1L, List.of());

        when(service.create(any()))
                .thenThrow(new ValidationException("the order must have at least one item"));

        mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("the order must have at least one item"));
    }

    @Test
    void get_shouldReturn200() throws Exception {
        var resp = new OrderResponse(5L, 1L, 1L, OrderStatus.CREATED,
                BigDecimal.valueOf(15000), List.of(), Instant.now(), Instant.now());

        when(service.get(5L)).thenReturn(resp);

        mvc.perform(get("/api/orders/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void get_shouldReturn404WhenNotFound() throws Exception {
        when(service.get(99L))
                .thenThrow(new ResourceNotFoundException("order 99 not found"));

        mvc.perform(get("/api/orders/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("order 99 not found"));
    }
    @Test
    void pay_shouldReturn200() throws Exception {
        var resp = new OrderResponse(1L, 1L, 1L, OrderStatus.PAID,
                BigDecimal.valueOf(15000), List.of(), Instant.now(), Instant.now());

        when(service.pay(1L)).thenReturn(resp);

        mvc.perform(put("/api/orders/1/pay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    void pay_shouldReturn400WhenInsufficientStock() throws Exception {
        when(service.pay(1L))
                .thenThrow(new BusinessException("insufficient stock for product 1"));

        mvc.perform(put("/api/orders/1/pay"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("insufficient stock for product 1"));
    }

    @Test
    void ship_shouldReturn200() throws Exception {
        var resp = new OrderResponse(1L, 1L, 1L, OrderStatus.SHIPPED,
                BigDecimal.valueOf(15000), List.of(), Instant.now(), Instant.now());

        when(service.ship(1L)).thenReturn(resp);

        mvc.perform(put("/api/orders/1/ship"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    @Test
    void deliver_shouldReturn200() throws Exception {
        var resp = new OrderResponse(1L, 1L, 1L, OrderStatus.DELIVERED,
                BigDecimal.valueOf(15000), List.of(), Instant.now(), Instant.now());

        when(service.deliver(1L)).thenReturn(resp);

        mvc.perform(put("/api/orders/1/deliver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void cancel_shouldReturn200() throws Exception {
        var resp = new OrderResponse(1L, 1L, 1L, OrderStatus.CANCELLED,
                BigDecimal.valueOf(15000), List.of(), Instant.now(), Instant.now());

        when(service.cancel(1L)).thenReturn(resp);

        mvc.perform(put("/api/orders/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void cancel_shouldReturn400WhenShipped() throws Exception {
        when(service.cancel(1L))
                .thenThrow(new BusinessException("shipped orders can't be cancelled"));

        mvc.perform(put("/api/orders/1/cancel"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("shipped orders can't be cancelled"));
    }
}
