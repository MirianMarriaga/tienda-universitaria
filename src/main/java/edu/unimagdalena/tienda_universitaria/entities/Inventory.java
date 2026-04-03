package edu.unimagdalena.tienda_universitaria.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "inventories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name= "available_stock", nullable = false)
    private Integer availableStock;

    @Column(name= "minimum_stock", nullable = false)
    private Integer minimumStock;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
