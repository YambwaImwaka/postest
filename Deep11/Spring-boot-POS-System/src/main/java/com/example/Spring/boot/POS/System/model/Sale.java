package com.example.Spring.boot.POS.System.model;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionCode;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private String paymentMethod;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    @JsonBackReference("product-saleitems")
    private List<SaleItem> items = new ArrayList<>();

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    // Add getters and setters
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // Getters and setters

    public void addItem(SaleItem item) {
        if (item != null) {
            items.add(item);// Add the item to the list
            item.setSale(this);
        } else {
            throw new IllegalArgumentException("SaleItem cannot be null");
        }
    }

    public List<SaleItem> getItems() {
        return items; // Getter for items
    }
}



