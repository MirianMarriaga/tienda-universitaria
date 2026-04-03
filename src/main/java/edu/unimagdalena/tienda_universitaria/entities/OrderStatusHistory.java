package edu.unimagdalena.tienda_universitaria.entities;

import edu.unimagdalena.tienda_universitaria.entities.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_status_histories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class OrderStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "changed_at", nullable = false)
    private OrderStatus changedAAt;
}
