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

public class OrderItemRepositoryIntegrationTest extends AbstractRepositoryIT{

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    AddressRepository addressRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    OrderItemRepository orderItemRepo;

    @Test
    @DisplayName("OrderItem = Search by order id")
    void shouldFindByOrder_Id(){
        //Given
        //quantity of each item in the orders
        var quantity1 = 2;
        var quantity2 = 3;
        var quantity3 = 3;

        var category = categoryRepo.save(Category.builder()
                .name("Books")
                .description("Academic books")
                .createdAt(Instant.now())
                .build());

        var customer = customerRepo.save(Customer.builder()
                .fullName("Juan Amador Hernandez")
                .identificationNumber("1001234567")
                .email("jahernandez@unimagdalena.edu.co")
                .phone("+57 310 456 7821")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var address = addressRepo.save(Address.builder()
                .customer(customer)
                .street("Calle 22 #5-30")
                .city("Santa Marta")
                .state("Magdalena")
                .country("Colombia")
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

        var product3 = productRepo.save(Product.builder()
                .sku("BOOK-CS-DATASTR-1ED-102")
                .category(category)
                .name("Data Structures Fundamentals")
                .description("Introduction to data structures")
                .price(BigDecimal.valueOf(52000.00))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        //unit prices of each product
        var unitPrice1 = product1.getPrice();
        var unitPrice2 = product2.getPrice();
        var unitPrice3 = product3.getPrice();

        //subtotal of each order item
        var subtotal1 = unitPrice1.multiply(BigDecimal.valueOf(quantity1));
        var subtotal2 = unitPrice2.multiply(BigDecimal.valueOf(quantity2));
        var subtotal3 = unitPrice3.multiply(BigDecimal.valueOf(quantity3));;

        //total of each order
        var order1Total = subtotal1.add(subtotal2);
        var order2total = subtotal3;

        var order1 = orderRepo.save(Order.builder()
                .customer(customer)
                .address(address)
                .status(OrderStatus.SHIPPED)
                .total(order1Total)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var order2 = orderRepo.save(Order.builder()
                .customer(customer)
                .address(address)
                .status(OrderStatus.DELIVERED)
                .total(order2total)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        var ordenItem1 = orderItemRepo.save(OrderItem.builder()
                .order(order1)
                .product(product1)
                .quantity(quantity1)
                .unitPrice(product1.getPrice())
                .subtotal(subtotal1)
                .createdAt(Instant.now())
                .build());

        var ordenItem2 = orderItemRepo.save(OrderItem.builder()
                .order(order1)
                .product(product2)
                .quantity(quantity2)
                .unitPrice(product2.getPrice())
                .subtotal(subtotal2)
                .createdAt(Instant.now())
                .build());

        var ordenItem3 = orderItemRepo.save(OrderItem.builder()
                .order(order2)
                .product(product3)
                .quantity(quantity3)
                .unitPrice(product3.getPrice())
                .subtotal(subtotal3)
                .createdAt(Instant.now())
                .build());

        //When
        List<OrderItem> result = orderItemRepo.findByOrder_Id(order1.getId());
        //Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getOrder().getId()).isEqualTo(order1.getId());
        assertThat(result.get(1).getOrder().getId()).isEqualTo(order1.getId());
        assertThat(result).hasSize(2);
    }
}

