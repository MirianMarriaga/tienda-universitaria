package edu.unimagdalena.tienda_universitaria.services;

import edu.unimagdalena.tienda_universitaria.api.dto.OrderDtos.*;

import edu.unimagdalena.tienda_universitaria.entities.Order;
import edu.unimagdalena.tienda_universitaria.entities.OrderItem;
import edu.unimagdalena.tienda_universitaria.entities.OrderStatusHistory;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import edu.unimagdalena.tienda_universitaria.repositories.*;
import edu.unimagdalena.tienda_universitaria.services.mapper.IOrderMapper;
import edu.unimagdalena.tienda_universitaria.exception.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{
    private final OrderRepository OrderRepo;
    private final CustomerRepository customerRepo;
    private final AddressRepository addressRepo;
    private final ProductRepository productRepo;
    private final InventoryRepository inventoryRepo;
    private final OrderStatusHistoryRepository historyRepo;
    private final OrderItemRepository itemRepo;
    private final IOrderMapper mapper;

    @Transactional
    @Override
    public OrderResponse create(OrderCreateRequest req) {

        if (req.items() == null || req.items().isEmpty())
            throw new ValidationException("The order must have at least one item");

        if (req.items().stream().anyMatch(i->i.quantity() <= 0))
            throw new ValidationException("all quantities must be greater than zero");

        var customer = customerRepo.findById(req.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer %d not found".formatted(req.customerId())));

        if(customer.getStatus() != CustomerStatus.ACTIVE)
            throw new BusinessException("Customer %d is not active".formatted(req.customerId()));

        var address = addressRepo.findById(req.addressId())
                .orElseThrow(()-> new ResourceNotFoundException("Address %d not found".formatted(req.addressId())));

        if(!address.getCustomer().getId().equals(req.customerId()))
            throw new ConflictException("The address does not belong to the customer");

        var order = Order.builder()
                .customer(customer)
                .address(address)
                .status(OrderStatus.CREATED)
                .total(BigDecimal.ZERO)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        var savedOrder = OrderRepo.save(order);


        List<OrderItem> items = req.items().stream().map(i -> {
            var product = productRepo.findById(i.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product %d not found".formatted(i.productId())));
            if (!product.getActive())
                throw new BusinessException("product %d is not active".formatted(i.productId()));
            var unitPrice = product.getPrice();
            var subtotal = unitPrice.multiply(BigDecimal.valueOf(i.quantity()));
            return OrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(i.quantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();
        }).toList();

        itemRepo.saveAll(items);

        var total = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        savedOrder.setTotal(total);
        savedOrder.setItems(items);
        var finalOrder = OrderRepo.save(savedOrder);

        historyRepo.save(OrderStatusHistory.builder()
                .order(finalOrder)
                .status(OrderStatus.CREATED)
                .changedAt(Instant.now())
                .build());

        return mapper.toResponse(finalOrder);
    }

    @Override @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        return OrderRepo.findById(id).map(o-> mapper.toResponse(o))
                .orElseThrow(()-> new ResourceNotFoundException("Order %d not found".formatted(id)));
    }

    @Transactional
    @Override
    public OrderResponse pay(Long id) {
        var order = OrderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order %d not found".formatted(id)));

        if(order.getStatus() != OrderStatus.CREATED)
            throw new BusinessException("only created orders can be paid");

        for (var item : order.getItems()) {
            var inventory = inventoryRepo.findByProduct_Id(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("inventory not found for product %d".formatted(item.getProduct().getId())));

            if (inventory.getAvailableStock() < item.getQuantity())
                throw new BusinessException("insufficient stock for product %d".formatted(item.getProduct().getId()));
        }

        for (var item: order.getItems()) {
            var inventory = inventoryRepo.findByProduct_Id(item.getProduct().getId()).get();
            inventory.setAvailableStock(inventory.getAvailableStock() - item.getQuantity());
            inventoryRepo.save(inventory);
        }

        order.setStatus(OrderStatus.PAID);
        order.setUpdatedAt(Instant.now());
        var savedOrder = OrderRepo.save(order);

        historyRepo.save(OrderStatusHistory.builder()
                .order(savedOrder)
                .status(OrderStatus.PAID)
                .changedAt(Instant.now())
                .build());

        return mapper.toResponse(savedOrder);
    }

    @Transactional
    @Override
    public OrderResponse cancel(Long id) {
        var order = OrderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order %d not found".formatted(id)));

        if (order.getStatus() == OrderStatus.SHIPPED)
            throw new BusinessException("Shipped orders can't be cancelled");

        if (order.getStatus() == OrderStatus.DELIVERED)
            throw new BusinessException(("Delivered orders can't be cancelled"));

        if (order.getStatus() == OrderStatus.PAID) {
            for (var item : order.getItems()) {
                var inventory = inventoryRepo.findByProduct_Id(item.getProduct().getId()).get();
                inventory.setAvailableStock(inventory.getAvailableStock() + item.getQuantity());
                inventoryRepo.save(inventory);
            }
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(Instant.now());
        var savedOrder = OrderRepo.save(order);

        historyRepo.save(OrderStatusHistory.builder().
                order(savedOrder)
                .status(OrderStatus.CANCELLED)
                .changedAt(Instant.now())
                .build());

        return mapper.toResponse(savedOrder);
    }

    @Transactional
    @Override
    public OrderResponse ship(Long id) {
        var order = OrderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Order %d not found".formatted(id))));

        if (order.getStatus() != OrderStatus.PAID)
            throw new BusinessException("only paid orders can be shipped");

        order.setStatus(OrderStatus.SHIPPED);
        order.setUpdatedAt(Instant.now());
        var saved = OrderRepo.save(order);

        historyRepo.save(OrderStatusHistory.builder()
                .order(order)
                .status(OrderStatus.SHIPPED)
                .changedAt(Instant.now())
                .build());

        return mapper.toResponse(saved);
    }

    @Transactional
    @Override
    public OrderResponse deliver(Long id) {
        var order = OrderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order %d not found".formatted(id)));

        if (order.getStatus() != OrderStatus.SHIPPED)
            throw new BusinessException("only shipped orders can be delivered");

        order.setStatus(OrderStatus.DELIVERED);
        order.setUpdatedAt(Instant.now());
        var saved = OrderRepo.save(order);

        historyRepo.save(OrderStatusHistory.builder()
                .order(saved)
                .status(OrderStatus.DELIVERED)
                .changedAt(Instant.now())
                .build());

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderRepo.findAll().stream().map(o-> mapper.toResponse(o)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByCustomerId(Long customerId) {
        return OrderRepo.findByCustomer_Id(customerId).stream()
                .map(o-> mapper.toResponse(o))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByStatus(OrderStatus status) {
        return OrderRepo.findByStatus(status).stream()
                .map(o -> mapper.toResponse(o))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByFilters(Long customerId, OrderStatus status, Instant startDate, Instant endDate, BigDecimal minTotal, BigDecimal maxTotal) {
        return OrderRepo.findOrdersByFilters(customerId, status, startDate, endDate, minTotal, maxTotal)
                .stream().map(o -> mapper.toResponse(o)).toList();
    }
}
