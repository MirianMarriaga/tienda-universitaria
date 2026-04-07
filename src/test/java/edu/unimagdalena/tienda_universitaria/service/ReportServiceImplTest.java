package edu.unimagdalena.tienda_universitaria.service;

import edu.unimagdalena.tienda_universitaria.api.dto.ReportDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Order;
import edu.unimagdalena.tienda_universitaria.entities.OrderStatusHistory;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import edu.unimagdalena.tienda_universitaria.repositories.*;
import edu.unimagdalena.tienda_universitaria.services.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

    @Mock ProductRepository productRepo;
    @Mock OrderRepository orderRepo;
    @Mock CustomerRepository customerRepo;
    @Mock CategoryRepository categoryRepo;
    @Mock OrderStatusHistoryRepository historyRepo;

    @InjectMocks
    ReportServiceImpl service;

    @Test
    void shouldReturnBestSellingProducts() {
        // Given
        List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[]{1L, "Algebra", 10L});
        when(productRepo.findProductsBestSoldByPeriod(any(), any())).thenReturn(rows);

        // When
        var result = service.bestSellingProducts(Instant.now().minusSeconds(3600), Instant.now());

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).productName()).isEqualTo("Algebra");
        assertThat(result.get(0).totalQuantitySold()).isEqualTo(10L);
    }

    @Test
    void shouldReturnEmptyWhenNoBestSellingProducts() {
        // Given
        when(productRepo.findProductsBestSoldByPeriod(any(), any())).thenReturn(new ArrayList<>());

        // When
        var result = service.bestSellingProducts(Instant.now().minusSeconds(3600), Instant.now());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnMonthlyIncome() {
        // Given
        List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[]{2024, 3, new BigDecimal("500000.00")});
        when(orderRepo.findByMonthlyRevenue()).thenReturn(rows);

        // When
        var result = service.monthlyIncome();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).year()).isEqualTo(2024);
        assertThat(result.get(0).month()).isEqualTo(3);
        assertThat(result.get(0).totalIncome()).isEqualByComparingTo(new BigDecimal("500000.00"));
    }

    @Test
    void shouldReturnTopCustomers() {
        // Given
        List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[]{1L, "Angelica Villegas", new BigDecimal("800000.00")});
        when(customerRepo.findTopByCustomer()).thenReturn(rows);

        // When
        var result = service.topCustomers();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).customerName()).isEqualTo("Angelica Villegas");
        assertThat(result.get(0).totalSpent()).isEqualByComparingTo(new BigDecimal("800000.00"));
    }

    @Test
    void shouldReturnLowStockProducts() {
        // Given
        List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[]{1L, "Algebra", 2, 10});
        when(productRepo.findByProductsInsufficientStock()).thenReturn(rows);

        // When
        var result = service.lowStockProducts();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).productName()).isEqualTo("Algebra");
        assertThat(result.get(0).availableStock()).isEqualTo(2);
        assertThat(result.get(0).minimumStock()).isEqualTo(10);
    }

    @Test
    void shouldReturnTopCategories() {
        // Given
        List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[]{1L, "Clothing", 15L});
        when(categoryRepo.findTopByCategory()).thenReturn(rows);

        // When
        var result = service.topCategories();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).categoryName()).isEqualTo("Clothing");
        assertThat(result.get(0).totalQuantitySold()).isEqualTo(15L);
    }

    @Test
    void shouldReturnOrderHistory() {
        // Given
        var order = Order.builder().id(1L).build();
        var history = OrderStatusHistory.builder()
                .id(1L)
                .order(order)
                .status(OrderStatus.CREATED)
                .changedAt(Instant.now())
                .build();

        when(historyRepo.findHistoryByOrdenId(1L)).thenReturn(List.of(history));
        when(orderRepo.existsById(1L)).thenReturn(true);

        // When
        var result = service.getHistory(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).orderId()).isEqualTo(1L);
        assertThat(result.get(0).status()).isEqualTo(OrderStatus.CREATED);
    }
}