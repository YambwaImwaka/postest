package com.example.Spring.boot.POS.System.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private String status;
    private String supplier;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<PurchaseOrderItem> items = new ArrayList<>();

    // Getters and setters
}
