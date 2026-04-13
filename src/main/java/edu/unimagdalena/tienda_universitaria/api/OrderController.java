package edu.unimagdalena.tienda_universitaria.api;

import edu.unimagdalena.tienda_universitaria.api.dto.OrderDtos.*;
import edu.unimagdalena.tienda_universitaria.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    /*
- [x ]  `POST /api/orders` — crear pedido
- [x ]  `GET /api/orders/{id}` — obtener pedido por id
- [x ]  `GET /api/orders` — listar todos los pedidos
- [ ]  `PUT /api/orders/{id}/pay` — pagar pedido
- [ ]  `PUT /api/orders/{id}/ship` — enviar pedido
- [ ]  `PUT /api/orders/{id}/deliver` — marcar como entregado
- [ ]  `PUT /api/orders/{id}/cancel` — cancelar pedido
     */

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderCreateRequest req,
                                                UriComponentsBuilder uriBuilder) {
        var body = service.create(req);
        var location = uriBuilder.path("/api/orders/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> list(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        var p = service.list(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(p);
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<OrderResponse> pay(@PathVariable Long id) {
        return ResponseEntity.ok(service.pay(id));
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderResponse> ship(@PathVariable Long id) {
        return ResponseEntity.ok(service.ship(id));
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliver(@PathVariable Long id) {
        return ResponseEntity.ok(service.deliver(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancel(id));
    }
}
