package edu.unimagdalena.tienda_universitaria.api;


import edu.unimagdalena.tienda_universitaria.api.dto.ReportDtos.*;
import edu.unimagdalena.tienda_universitaria.services.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import tools.jackson.databind.ObjectMapper;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    ReportService service;

    @Test
    void bestSellingProducts_shouldReturn200() throws Exception {
        var resp = new BestSellingProductResponse(1L, "Algebra", 10L);

        when(service.bestSellingProducts(any(), any())).thenReturn(List.of(resp));

        mvc.perform(get("/api/reports/best-selling-products")
                        .param("start", Instant.now().minusSeconds(3600).toString())
                        .param("end", Instant.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Algebra"))
                .andExpect(jsonPath("$[0].totalQuantitySold").value(10));
    }

    @Test
    void monthlyIncome_shouldReturn200() throws Exception {
        var resp = new MonthlyIncomeResponse(2024, 3, new BigDecimal("500000.00"));

        when(service.monthlyIncome()).thenReturn(List.of(resp));

        mvc.perform(get("/api/reports/monthly-income"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].year").value(2024))
                .andExpect(jsonPath("$[0].month").value(3));
    }

    @Test
    void topCustomers_shouldReturn200() throws Exception {
        var resp = new TopCustomerResponse(1L, "Zaywel", new BigDecimal("500.0"));

        when(service.topCustomers()).thenReturn(List.of(resp));

        mvc.perform(get("/api/reports/top-customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("Zaywel"));
    }

    @Test
    void lowStockProducts_shouldReturn200() throws Exception {
        var resp = new LowStockProductResponse(1L, "Gravity Falls", 2, 10);

        when(service.lowStockProducts()).thenReturn(List.of(resp));

        mvc.perform(get("/api/reports/low-stock-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Gravity Falls"))
                .andExpect(jsonPath("$[0].availableStock").value(2));
    }

    @Test
    void topCategories_shouldReturn200() throws Exception {
        var resp = new TopCategoryResponse(1L, "Clothing", 15L);

        when(service.topCategories()).thenReturn(List.of(resp));

        mvc.perform(get("/api/reports/top-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryName").value("Clothing"))
                .andExpect(jsonPath("$[0].totalQuantitySold").value(15));
    }

}
