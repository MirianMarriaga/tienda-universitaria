package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.OrderDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.*;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import edu.unimagdalena.tienda_universitaria.repositories.*;
import edu.unimagdalena.tienda_universitaria.services.mapper.IOrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    OrderRepository orderRepo;
    @Mock
    CustomerRepository customerRepo;
    @Mock
    AddressRepository addressRepo;
    @Mock
    ProductRepository productRepo;
    @Mock
    InventoryRepository inventoryRepo;
    @Mock
    OrderStatusHistoryRepository historyRepo;
    @Mock
    OrderItemRepository itemRepo;
    @InjectMocks
    OrderServiceImpl service;
    @Spy private IOrderMapper mapper = Mappers.getMapper(IOrderMapper.class);

    @Test
    void shouldCreateAndReturnResponseDto() {
        var customer = Customer.builder()
                .id(1L)
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .email("jahernandez@unimagdalena.edu.co")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .build();

        var address = Address.builder()
                .id(1L)
                .customer(customer)
                .street("Calle 22 #5-30")
                .city("Santa Marta")
                .state("Magdalena")
                .country("Colombia")
                .build();

        var category = Category.builder()
                .name("Books")
                .description("Academic books")
                .createdAt(Instant.now())
                .build();

        var product = Product.builder()
                .id(1L)
                .sku("BOOK-ENG-ALG-2ED-045")
                .category(category)
                .name("Engineering Algebra")
                .description("Algebra book")
                .price(BigDecimal.valueOf(48000.00))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        var req = new OrderCreateRequest(
                1L,
                1L,
                List.of(new OrderCreateItemRequest(1L,2))
        );

        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(addressRepo.findById(1L)).thenReturn(Optional.of(address));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));

        when(orderRepo.save(any())).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(1L);
            return o;
        });

        when (itemRepo.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));

        var res = service.create(req);

        assertThat(res.total()).isEqualTo(BigDecimal.valueOf(96000.00));
        assertThat(res.status()).isEqualTo(OrderStatus.CREATED);
        verify(orderRepo, times(2)).save(any());
        verify(itemRepo).saveAll(any());
        verify(historyRepo).save(any());
    }

    @Test
    void shouldDiscountInventoryWhenPaid() {
        var product = Product.builder()
                .id(1L)
                .sku("BOOK-ENG-ALG-2ED-045")
                .name("Engineering Algebra")
                .price(BigDecimal.valueOf(48000.00))
                .active(true)
                .build();

        var item = OrderItem.builder()
                .product(product)
                .quantity(2)
                .unitPrice(product.getPrice())
                .subtotal(product.getPrice().multiply(BigDecimal.valueOf(2)))
                .build();

        var order = Order.builder()
                .id(1L)
                .status(OrderStatus.CREATED)
                .total(item.getSubtotal())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .items(List.of(item))
                .build();

        var inventory = Inventory.builder()
                .product(product)
                .availableStock(10)
                .minimumStock(2)
                .updatedAt(Instant.now())
                .build();

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepo.findByProduct_Id(1L)).thenReturn(Optional.of(inventory));
        when(orderRepo.save(any())).thenReturn(order);

        service.pay(1L);

        assertThat(inventory.getAvailableStock()).isEqualTo(8);
        verify(inventoryRepo).save(inventory);
        verify(historyRepo).save(any());
    }

    @Test
    void shouldShipOrderWhenPaid() {
        var order = Order.builder()
                .id(1L)
                .status(OrderStatus.PAID)
                .total(BigDecimal.valueOf(48000.00))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepo.save(any())).thenReturn(order);

        var result = service.ship(1L);

        assertThat(result.status()).isEqualTo(OrderStatus.SHIPPED);
        verify(historyRepo).save(any());
    }

    @Test
    void shouldDeliverOrderWhenShipped() {
        var order = Order.builder()
                .id(1L)
                .status(OrderStatus.SHIPPED)
                .total(BigDecimal.valueOf(48000.00))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepo.save(any())).thenReturn(order);

        var result = service.deliver(1L);

        assertThat(result.status()).isEqualTo(OrderStatus.DELIVERED);;
        verify(historyRepo).save(any());
    }

    @Test
    void shouldRevertStockWhenCancelledAfterPaid() {
        var product = Product.builder()
                .id(1L)
                .sku("BOOK-ENG-ALG-2ED-045")
                .name("Engineering Algebra")
                .price(BigDecimal.valueOf(48000.00))
                .active(true)
                .build();

        var item = OrderItem.builder()
                .product(product)
                .quantity(3)
                .unitPrice(product.getPrice())
                .subtotal(product.getPrice().multiply(BigDecimal.valueOf(3)))
                .build();

        var order = Order.builder()
                .id(1L)
                .status(OrderStatus.PAID)
                .total(item.getSubtotal())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .items(List.of(item))
                .build();

        var inventory = Inventory.builder()
                .product(product)
                .availableStock(7)
                .minimumStock(2)
                .updatedAt(Instant.now())
                .build();

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepo.findByProduct_Id(1L)).thenReturn(Optional.of(inventory));
        when(orderRepo.save(any())).thenReturn(order);

        service.cancel(1L);

        assertThat(inventory.getAvailableStock()).isEqualTo(10);
        verify(inventoryRepo).save(inventory);
        verify(historyRepo).save(any());
    }

    @Test
    void shouldFindOrdersByCustomersId() {
        var customer = Customer.builder()
                .id(1L)
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .email("jahernandez@unimagdalena.edu.co")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .build();

        var address = Address.builder()
                .id(1L)
                .customer(customer)
                .street("Calle 22 #5-30")
                .city("Santa Marta")
                .state("Magdalena")
                .country("Colombia")
                .build();

        var order = Order.builder()
                .id(1L)
                .customer(customer)
                .address(address)
                .status(OrderStatus.CREATED)
                .total(BigDecimal.valueOf(48000.00))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(orderRepo.findByCustomer_Id(1L)).thenReturn(List.of(order));

        var result = service.findByCustomerId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).customerId()).isEqualTo(1L);
    }

    @Test
    void shouldFindOrdersByStatus() {
        var customer = Customer.builder()
                .id(1L)
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .email("jahernandez@unimagdalena.edu.co")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .build();

        var address = Address.builder()
                .id(1L)
                .customer(customer)
                .street("Calle 22 #5-30")
                .city("Santa Marta")
                .state("Magdalena")
                .country("Colombia")
                .build();

        var order = Order.builder()
                .id(1L)
                .customer(customer)
                .address(address)
                .status(OrderStatus.PAID)
                .total(BigDecimal.valueOf(48000.00))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(orderRepo.findByStatus(OrderStatus.PAID)).thenReturn(List.of(order));

        var result = service.findByStatus(OrderStatus.PAID);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void shouldFindOrdersByFilters() {
        var customer = Customer.builder()
                .id(1L)
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .email("jahernandez@unimagdalena.edu.co")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .build();

        var address = Address.builder()
                .id(1L)
                .customer(customer)
                .street("Calle 22 #5-30")
                .city("Santa Marta")
                .state("Magdalena")
                .country("Colombia")
                .build();

        var order = Order.builder()
                .id(1L)
                .customer(customer)
                .address(address)
                .status(OrderStatus.PAID)
                .total(BigDecimal.valueOf(48000.00))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(orderRepo.findOrdersByFilters(
                any(), any(), any(), any(), any(), any()
        )).thenReturn(List.of(order));

        var result = service.findByFilters(
                null,
                OrderStatus.PAID,
                null,
                null,
                null,
                null
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(OrderStatus.PAID);
    }

}
