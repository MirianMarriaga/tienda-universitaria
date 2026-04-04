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

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRepositoryIntegrationTest extends AbstractRepositoryIT {

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    OrderItemRepository orderItemRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    AddressRepository addressRepo;

    @Test
    @DisplayName("Category: Search top category by sales")
    void findTopByCategory(){
        //Given
        var customer1 = customerRepo.save(Customer.builder()
                .fullName("Angelica Villegas")
                .identificationNumber("1001234567")
                .email("angelicamvg@gmail.com")
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

        var category1 = categoryRepo.save(Category.builder()
                .name("Clothing")
                .description("University clothing")
                .createdAt(Instant.now())
                .build());

        var category2 = categoryRepo.save(Category.builder()
                .name("Books")
                .description("University books")
                .createdAt(Instant.now())
                .build());

        var product1 = productRepo.save(Product.builder()
                .sku("CLO-001")
                .category(category1)
                .name("shirt")
                .description("Soccer team shirt")
                .price(new BigDecimal("35000.00"))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var product2 = productRepo.save(Product.builder()
                .sku("BOO-001")
                .category(category2)
                .name("Calculus")
                .description("Calculus book")
                .price(new BigDecimal("250000.00"))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var order1 = orderRepo.save(Order.builder()
                .customer(customer1)
                .address(address)
                .status(OrderStatus.PAID)
                .total(new BigDecimal("320000.00"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        orderItemRepo.save(OrderItem.builder()
                .order(order1)
                .product(product1)
                .quantity(2)
                .unitPrice(new BigDecimal("35000.00"))
                .subtotal(new BigDecimal("70000.00"))
                .createdAt(Instant.now())
                .build());

        orderItemRepo.save(OrderItem.builder()
                .order(order1)
                .product(product2)
                .quantity(1)
                .unitPrice(new BigDecimal("250000.00"))
                .subtotal(new BigDecimal("250000.00"))
                .createdAt(Instant.now())
                .build());

        //When
        List<Object[]> result = categoryRepo.findTopByCategory();

        //Then
        assertThat(result).hasSize(2);
        assertThat((String) result.get(0)[1]).isEqualTo("Clothing");

    }

}
