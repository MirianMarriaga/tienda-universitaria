package edu.unimagdalena.tienda_universitaria.service.mapper;

import edu.unimagdalena.tienda_universitaria.entities.*;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import edu.unimagdalena.tienda_universitaria.services.mapper.IOrderMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderMapperTest {
    private final IOrderMapper mapper = Mappers.getMapper(IOrderMapper.class);

    @Test
    void toResponse_shouldMapOrder() {
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

        var result = mapper.toResponse(order);

        assertThat(result.customerId()).isEqualTo(1L);
        assertThat(result.addressId()).isEqualTo(1L);
        assertThat(result.status()).isEqualTo(OrderStatus.CREATED);
        assertThat(result.total()).isEqualTo(BigDecimal.valueOf(48000.00));
    }

    @Test
    void toResponse_shouldMapOrderItem() {
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
                .total(BigDecimal.ZERO)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
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
                .price(BigDecimal.valueOf(45000.00))
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        var unitPrice = product.getPrice();

        var item = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(2)
                .unitPrice(unitPrice)
                .subtotal(unitPrice.multiply(BigDecimal.valueOf(2)))
                .build();
        var result = mapper.toResponse(item);

        assertThat(result.productId()).isEqualTo(1L);
        assertThat(result.productName()).isEqualTo("Engineering Algebra");
        assertThat(result.quantity()).isEqualTo(2);
        assertThat(result.subtotal()).isEqualTo(BigDecimal.valueOf(90000.00));
    }
}
