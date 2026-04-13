package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.InventoryDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Inventory;
import edu.unimagdalena.tienda_universitaria.entities.Product;
import edu.unimagdalena.tienda_universitaria.repositories.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {

    @Mock
    InventoryRepository inventoryRepo;
    @InjectMocks
    InventoryServiceImpl service;

    @Test
    void shouldUpdateInventoryWithNewValues() {
        var product = Product.builder()
                .id(1L)
                .sku("BOOK-ENG-ALG-2ED-045")
                .name("Engineering Algebra")
                .price(BigDecimal.valueOf(45000.00))
                .active(true)
                .build();

        var inventory = Inventory.builder()
                .id(1L)
                .product(product)
                .availableStock(10)
                .minimumStock(2)
                .updatedAt(Instant.now())
                .build();

        when(inventoryRepo.findByProduct_Id(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepo.save(any())).thenReturn(inventory);

        var req = new InventoryUpdateRequest(20,5);

        var result = service.update(1L, req);

        assertThat(result.availableStock()).isEqualTo(20);
        assertThat(result.minimumStock()).isEqualTo(5);
        verify(inventoryRepo).save(any());
    }

    @Test
    void shouldUpdateInventoryIgnoringNullValues() {
        var product = Product.builder()
                .id(1L)
                .sku("BOOK-ENG-ALG-2ED-045")
                .name("Engineering Algebra")
                .price(BigDecimal.valueOf(45000.00))
                .active(true)
                .build();

        var inventory = Inventory.builder()
                .id(1L)
                .product(product)
                .availableStock(10)
                .minimumStock(2)
                .updatedAt(Instant.now())
                .build();

        when(inventoryRepo.findByProduct_Id(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepo.save(any())).thenReturn(inventory);

        var req = new InventoryUpdateRequest(null, 5);

        var result = service.update(1L, req);

        assertThat(result.availableStock()).isEqualTo(10);
        assertThat(result.minimumStock()).isEqualTo(5);
    }
}
