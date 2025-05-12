package com.example.Spring.boot.POS.System.model;

/*import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private String category;
    private int reorderLevel;
    private String supplier;


    // Getters and setters
}*/

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;


import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Product {
    public int getStockQuantity;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(name = "stock_quantity", nullable = false)
    //private Integer stockQuantity;


    //private String sku;
    //private String name;
    private String description;
    // private BigDecimal price;
    // private int stockQuantity;
    private String category;
    //private int reorderLevel;
    private String supplier;

    @Column(unique = true)
    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Product name is required")
    private String name;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int stockQuantity;

    @Min(value = 0, message = "Reorder level cannot be negative")
    private int reorderLevel;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("product-saleitems")
    private List<SaleItem> saleItems = new ArrayList<>();
    //@OneToMany(mappedBy = "product")
    //@JsonManagedReference("product-saleitems")
    //private List<SaleItem> saleItems = new ArrayList<>();


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<PurchaseOrderItem> purchaseOrderItems = new ArrayList<>();

   /* @Column(name = "stock_quantity")
    private int stockQuantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;*/

    // Existing getters and setters

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItems() {
        return purchaseOrderItems;
    }

    
    // Helper methods for bidirectional relationship management
    public void addSaleItem(SaleItem saleItem) {
        saleItems.add(saleItem);
        saleItem.setProduct(this);
    }

    public void removeSaleItem(SaleItem saleItem) {
        saleItems.remove(saleItem);
        saleItem.setProduct(null);
    }

    public void addPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        purchaseOrderItems.add(purchaseOrderItem);
        purchaseOrderItem.setProduct(this);
    }

    public void removePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        purchaseOrderItems.remove(purchaseOrderItem);
        purchaseOrderItem.setProduct(null);
    }
}
