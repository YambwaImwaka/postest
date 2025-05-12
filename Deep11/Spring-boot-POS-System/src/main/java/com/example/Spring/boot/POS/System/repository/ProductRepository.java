package com.example.Spring.boot.POS.System.repository;

import com.example.Spring.boot.POS.System.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


import java.math.BigDecimal;


public interface ProductRepository extends JpaRepository<Product, Long> {
    //List<Product> findByNameContainingIgnoreCase(String name);
    //List<Product> findBySkuContainingIgnoreCase(String sku);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findBySkuContainingIgnoreCase(String sku);
    //List<Product> findByStockQuantityLessThan(int quantity);
    List<Product> findByCategory(String category);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity < :threshold")
    List<Product> findLowStockProducts(@Param("threshold") int threshold);

   // @Query("SELECT p FROM Product p WHERE p.stockQuantity = 0")
    //List<Product> findOutOfStockProducts();

    // Find products with stock less than specified quantity
    //List<Product> findByStockQuantityLessThan(int quantity);

    // Find products with exact stock quantity
    List<Product> findByStockQuantity(int quantity);

    // Calculate total inventory value
    //@Query("SELECT SUM(p.stockQuantity * p.price) FROM Product p")
    //BigDecimal calculateTotalInventoryValue();


    @Query("SELECT p FROM Product p WHERE p.stockQuantity < :threshold")
    List<Product> findByStockQuantityLessThan(@Param("threshold") int threshold);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity = 0")
    List<Product> findByStockQuantityZero();

    @Query("SELECT COALESCE(SUM(p.stockQuantity * p.price), 0) FROM Product p")
    BigDecimal calculateTotalInventoryValue();





}