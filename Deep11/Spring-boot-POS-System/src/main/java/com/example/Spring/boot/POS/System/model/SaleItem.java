package com.example.Spring.boot.POS.System.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Entity
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    @JsonBackReference("sale-items")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference("sale-items")
    private Product product;

    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    // Getters and setters
}
