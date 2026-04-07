package edu.unimagdalena.tienda_universitaria.services;


import edu.unimagdalena.tienda_universitaria.api.dto.ReportDtos.*;
import edu.unimagdalena.tienda_universitaria.exception.ResourceNotFoundException;
import edu.unimagdalena.tienda_universitaria.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;
    private final CategoryRepository categoryRepo;
    private final OrderStatusHistoryRepository historyRepo;

    @Override
    public List<BestSellingProductResponse> bestSellingProducts(Instant start, Instant end) {
        return productRepo.findProductsBestSoldByPeriod(start, end).stream()
                .map(row -> new BestSellingProductResponse(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        ((Number) row[2]).longValue()
                ))
                .toList();
    }

    @Override
    public List<MonthlyIncomeResponse> monthlyIncome() {
        return orderRepo.findByMonthlyRevenue().stream()
                .map(row -> new MonthlyIncomeResponse(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue(),
                        (BigDecimal) row[2]
                ))
                .toList();
    }

    @Override

    public List<OrderStatusHistoryResponse> getHistory(Long orderId) {

        if  (!orderRepo.existsById(orderId)) {
            throw new ResourceNotFoundException("Order %d not found".formatted(orderId));
        }

        return historyRepo.findHistoryByOrdenId(orderId).stream()
                .map(h -> new OrderStatusHistoryResponse(
                        h.getId(),
                        h.getOrder().getId(),
                        h.getStatus(),
                        h.getChangedAt()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopCustomerResponse> topCustomers() {
        return customerRepo.findTopByCustomer().stream()
                .map(row -> new TopCustomerResponse(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (BigDecimal) row[2]
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LowStockProductResponse> lowStockProducts() {
        return productRepo.findByProductsInsufficientStock().stream()
                .map(row -> new LowStockProductResponse(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        ((Number) row[2]).intValue(),
                        ((Number) row[3]).intValue()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopCategoryResponse> topCategories() {
        return categoryRepo.findTopByCategory().stream()
                .map(row -> new TopCategoryResponse(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        ((Number) row[2]).longValue()
                ))
                .toList();
    }

}
