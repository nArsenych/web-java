package com.example.spacecatsmarket.repository.entity;

import com.example.spacecatsmarket.common.ProductType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_entries")
public class OrderEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    @Column(nullable = false)
    private int amount;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}
