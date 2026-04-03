package edu.unimagdalena.tienda_universitaria.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "SKU", nullable = false, unique = true)
    private String sku;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean active;

    @OneToOne(mappedBy = "product")
    private Inventory inventory;

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();
}
