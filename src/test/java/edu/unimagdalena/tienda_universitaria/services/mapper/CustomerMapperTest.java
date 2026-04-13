package edu.unimagdalena.tienda_universitaria.services.mapper;
import edu.unimagdalena.tienda_universitaria.api.dto.CustomerDtos.*;
import edu.unimagdalena.tienda_universitaria.entities.Customer;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerMapperTest {
    private final ICustomerMapper mapper = Mappers.getMapper(ICustomerMapper.class);

    @Test
    void toEntity_shouldMapCreateRequest() {
        var req = new CustomerCreateRequest(
                "1001234567",
                "Angelica Villegas",
                "angelicamvg@gmail.com",
                "+57 311 257 6767",
                null
        );

        var result = mapper.toEntity(req);

        assertThat(result.getFullName()).isEqualTo("Angelica Villegas");
        assertThat(result.getEmail()).isEqualTo("angelicamvg@gmail.com");
        assertThat(result.getStatus()).isNull();
        assertThat(result.getCreatedAt()).isNull();
    }

    @Test
    void toResponse_shouldMapEntity() {
        var customer = Customer.builder()
                .email("angelicamvg@gmail.com")
                .fullName("Angelica Villegas")
                .identificationNumber("1001234567")
                .phone("+57 311 254 3939")
                .status(CustomerStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        var result = mapper.toResponse(customer);

        assertThat(result.email()).isEqualTo("angelicamvg@gmail.com");
        assertThat(result.fullName()).isEqualTo("Angelica Villegas");
        assertThat(result.status()).isEqualTo(CustomerStatus.ACTIVE);
    }


    @Test
    void patch_shouldIgnoreNullFields() {
        var customer = Customer.builder()
                .email("angelicamvg@gmail.com")
                .fullName("Angelica Villegas")
                .phone("+57 311 254 3939")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        var changes = new CustomerUpdateRequest(
                "Angelica Sofia",
                null,
                null,
                null
        );

        mapper.patch(customer, changes);

        assertThat(customer.getEmail()).isEqualTo("angelicamvg@gmail.com");
        assertThat(customer.getFullName()).isEqualTo("Angelica Sofia");
    }

}
