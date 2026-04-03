package edu.unimagdalena.tienda_universitaria.entities;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name= "stock_available", nullable = false)
    private String stockAvailable;

    @Column(name= "minimum_stack", nullable = false)
    private String minimumStack;
}
