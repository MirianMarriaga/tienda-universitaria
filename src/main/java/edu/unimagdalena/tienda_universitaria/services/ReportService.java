package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.ReportDtos.*;

import java.time.Instant;
import java.util.List;

public interface ReportService {

    List<BestSellingProductResponse> bestSellingProducts(Instant start, Instant end);
    List<MonthlyIncomeResponse> monthlyIncome();
    List<TopCustomerResponse> topCustomers();
    List<LowStockProductResponse> lowStockProducts();
    List<OrderStatusHistoryResponse> getHistory(Long orderId);
    List<TopCategoryResponse> topCategories();
    
}
