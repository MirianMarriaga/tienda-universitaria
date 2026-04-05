package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.*;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryIntegrationTest extends AbstractRepositoryIT{

    @Autowired
    ProductRepository productRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    InventoryRepository inventoryRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    OrderItemRepository orderItemRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    AddressRepository addressRepo;



    @Test
    @DisplayName("Product: Search by SKU")
    void shouldFindBySku(){
        // Given
        var category = categoryRepo.save(Category.builder()
                .name("Books")
                .description("Academic books")
                .createdAt(Instant.now())
                .build());

        productRepo.save(Product.builder()
                .sku("BOOK-ENG-ALG-2ED-045")
                .category(category)
                .name("Engineering Algebra")
                .description("Algebra book")
                .price(BigDecimal.valueOf(45000))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        productRepo.save(Product.builder()
                .sku("BOOK-ENG-CAL-7ED-005")
                .category(category)
                .name("Differential Calculus")
                .description("Calculus book")
                .price(BigDecimal.valueOf(48000))
                .active(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        //When
        Optional<Product> result = productRepo.findBySku("BOOK-ENG-ALG-2ED-045");

        //Then
        assertThat(result).isPresent();
        assertThat(result.get().getSku()).isEqualTo("BOOK-ENG-ALG-2ED-045");
    }

    @Test
    @DisplayName("Product: Search by category")
    void shouldFindByCategory_IdAndActiveTrue(){
        // Given
        var category = categoryRepo.save(Category.builder()
                .name("Books")
                .description("Academic books")
                .createdAt(Instant.now())
                .build());

        productRepo.save(Product.builder()
                .sku("BOOK-ENG-ALG-2ED-045")
                .category(category)
                .name("Engineering Algebra")
                .description("Algebra book")
                .price(BigDecimal.valueOf(45000.00))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        productRepo.save(Product.builder()
                .sku("BOOK-ENG-CAL-7ED-005")
                .category(category)
                .name("Differential Calculus")
                .description("Calculus book")
                .price(BigDecimal.valueOf(48000.00))
                .active(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        // When
        List<Product> result = productRepo.findByCategory_IdAndActiveTrue(category.getId());

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActive()).isTrue();
        assertThat(result.get(0).getCategory().getId())
                .isEqualTo(category.getId());
    }

    @Test
    @DisplayName("Product: Search by low stock")
    void findByInventory_AvailableStockLessThan(){
        //Given
        var category = categoryRepo.save(Category.builder()
                .name("Books")
                .description("Academic books")
                .createdAt(Instant.now())
                .build());

        var product1 = productRepo.save(Product.builder()
                .sku("BOOK-ENG-ALG-2ED-045")
                .category(category)
                .name("Engineering Algebra")
                .description("Algebra book")
                .price(BigDecimal.valueOf(45000.00))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var product2 = productRepo.save(Product.builder()
                .sku("BOOK-ENG-CAL-7ED-005")
                .category(category)
                .name("Differential Calculus")
                .description("Calculus book")
                .price(BigDecimal.valueOf(48000.00))
                .active(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());


        var inventory1 = inventoryRepo.save(Inventory.builder()
                .product(product1)
                .availableStock(3)
                .minimumStock(5)
                .updatedAt(Instant.now())
                .build());

        var inventory2 = inventoryRepo.save(Inventory.builder()
                .product(product2)
                .availableStock(3)
                .minimumStock(5)
                .updatedAt(Instant.now())
                .build());

        // When
        List<Product> result = productRepo.findByInventory_AvailableStockLessThan(5);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Product: Search best sold by period")
    void findProductsBestSoldByPeriod(){
        //Given
        var category = categoryRepo.save(Category.builder()
                .name("Books")
                .description("Academic books")
                .createdAt(Instant.now())
                .build());

        var product1 = productRepo.save(Product.builder()
                .sku("BOOK-ENG-ALG-2ED-045")
                .category(category)
                .name("Engineering Algebra")
                .description("Algebra book")
                .price(BigDecimal.valueOf(45000.00))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var product2 = productRepo.save(Product.builder()
                .sku("BOOK-ENG-CAL-7ED-005")
                .category(category)
                .name("Differential Calculus")
                .description("Calculus book")
                .price(BigDecimal.valueOf(48000.00))
                .active(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var customer1 = customerRepo.save(Customer.builder()
                .fullName("Angelica Villegas")
                .email("angelicamvg@gmail.com")
                .identificationNumber("1001234567")
                .phone("+57 311 254 3939")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var address = addressRepo.save(Address.builder()
                .customer(customer1)
                .street("Mz G casa 6 Barrio Santa Cruz")
                .city("Santa Marta")
                .state("Magdalena")
                .country("Colombia")
                .createdAt(Instant.now())
                .build());

        var order = orderRepo.save(Order.builder()
                .customer(customer1)
                .address(address)
                .status(OrderStatus.PAID)
                .total(new BigDecimal("550000.00"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        orderItemRepo.save(OrderItem.builder()
                .order(order)
                .product(product1)
                .quantity(1)
                .unitPrice(new BigDecimal(250000.00))
                .subtotal(new BigDecimal(250000.00))
                .createdAt(Instant.now())
                .build());

        orderItemRepo.save(OrderItem.builder()
                .order(order)
                .product(product2)
                .quantity(2)
                .unitPrice(new BigDecimal(150000.00))
                .subtotal(new BigDecimal(300000.00))
                .createdAt(Instant.now())
                .build());

        // When
        Instant start = Instant.now().minusSeconds(60 * 60 * 24 * 30);
        Instant end   = Instant.now().plusSeconds(60);
        List<Object[]> result = productRepo.findProductsBestSoldByPeriod(start, end);

        // Then
        assertThat(result).isNotEmpty();
        assertThat((String) result.get(0)[1]).isEqualTo("Differential Calculus");
    }

    @Test
    @DisplayName("Product: Search product with insufficient stock")
    void findByProductsInsufficientStock() {
        //Given
        var category = categoryRepo.save(Category.builder()
                .name("Books")
                .description("Academic books")
                .createdAt(Instant.now())
                .build());

        var product1 = productRepo.save(Product.builder()
                .sku("BOOK-ENG-ALG-2ED-045")
                .category(category)
                .name("Engineering Algebra")
                .description("Algebra book")
                .price(BigDecimal.valueOf(45000.00))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var product2 = productRepo.save(Product.builder()
                .sku("BOOK-ENG-CAL-7ED-005")
                .category(category)
                .name("Differential Calculus")
                .description("Calculus book")
                .price(BigDecimal.valueOf(48000.00))
                .active(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        inventoryRepo.save(Inventory.builder()
                .product(product1)
                .availableStock(2)
                .minimumStock(10)
                .updatedAt(Instant.now())
                .build());

        inventoryRepo.save(Inventory.builder()
                .product(product2)
                .availableStock(50)
                .minimumStock(10)
                .updatedAt(Instant.now())
                .build());

        //When
        List<Object[]> result = productRepo.findByProductsInsufficientStock();
        assertThat(result).hasSize(1);
        assertThat((String) result.get(0)[1]).isEqualTo("Engineering Algebra");
        assertThat((Integer) result.get(0)[2]).isEqualTo(2);
        assertThat((Integer) result.get(0)[3]).isEqualTo(10);
    }
}
