package edu.unimagdalena.tienda_universitaria.api;

import edu.unimagdalena.tienda_universitaria.api.dto.AddressDtos.*;
import edu.unimagdalena.tienda_universitaria.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
@RequiredArgsConstructor
@Validated
public class AddressController{

    private final AddressService service;

    @PostMapping
    public ResponseEntity<AddressResponse> create(@PathVariable Long customerId, @Valid @RequestBody AddressCreateRequest req, UriComponentsBuilder uriBuilder){

        var created = service.create(customerId, req);
        var location = uriBuilder.path("/api/customers/{customerId}/addresses/{id}").buildAndExpand(customerId, created.id()).toUri();
        return ResponseEntity.created(location).body(created);

    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> listByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.listByCustomer(customerId));
    }

}
