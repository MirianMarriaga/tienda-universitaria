package edu.unimagdalena.tienda_universitaria.api;


import edu.unimagdalena.tienda_universitaria.api.dto.ReportDtos.*;
import edu.unimagdalena.tienda_universitaria.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportService service;

    @GetMapping("/best-selling-products")
    public ResponseEntity<List<BestSellingProductResponse>> bestSellingProducts(@RequestParam Instant start, @RequestParam Instant end) {
        return ResponseEntity.ok(service.bestSellingProducts(start, end));
    }

    @GetMapping("/monthly-income")
    public ResponseEntity<List<MonthlyIncomeResponse>> monthlyIncome() {
        return ResponseEntity.ok(service.monthlyIncome());
    }

    @GetMapping("/top-customers")
    public ResponseEntity<List<TopCustomerResponse>> topCustomers() {
        return ResponseEntity.ok(service.topCustomers());
    }

    @GetMapping("/low-stock-products")
    public ResponseEntity<List<LowStockProductResponse>> lowStockProducts() {
        return ResponseEntity.ok(service.lowStockProducts());
    }

    @GetMapping("/top-categories")
    public ResponseEntity<List<TopCategoryResponse>> topCategories() {
        return ResponseEntity.ok(service.topCategories());
    }

}
