package com.example.Spring.boot.POS.System.service;

//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
import com.example.Spring.boot.POS.System.dto.SaleItemDTO;
import com.example.Spring.boot.POS.System.exception.CustomerNotFoundException;
import com.example.Spring.boot.POS.System.exception.InsufficientStockException;
import com.example.Spring.boot.POS.System.model.SaleItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.example.Spring.boot.POS.System.model.Product;
import com.example.Spring.boot.POS.System.model.Sale;
import com.example.Spring.boot.POS.System.model.User;
import com.example.Spring.boot.POS.System.dto.SaleDTO;
import com.example.Spring.boot.POS.System.repository.ProductRepository;
import com.example.Spring.boot.POS.System.repository.SaleRepository;
import com.example.Spring.boot.POS.System.repository.UserRepository;

import com.example.Spring.boot.POS.System.repository.SaleRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Service
@Transactional

public class SalesService {
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final CustomerService customerService;
    //private final SalesRepository salesRepository;

    public SalesService(ProductRepository productRepository,
                        SaleRepository saleRepository,
                        UserRepository userRepository,
                        CustomerService customerService) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.customerService = customerService;
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public Sale processSale(SaleDTO saleDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setTransactionCode(generateTransactionCode());
        sale.setPaymentMethod(saleDTO.getPaymentMethod());

        BigDecimal subtotal = BigDecimal.ZERO;

        for (SaleItemDTO itemDTO : saleDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStockQuantity() < itemDTO.getQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for product: " + product.getName() +
                                " (Available: " + product.getStockQuantity() + ")"
                );
            }

            SaleItem item = new SaleItem();
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

            subtotal = subtotal.add(item.getSubtotal());
            sale.addItem(item);

            // Update stock
            product.setStockQuantity(product.getStockQuantity() - itemDTO.getQuantity());
            productRepository.save(product);
        }

        // Calculate totals
        sale.setTotalAmount(subtotal.add(saleDTO.getTaxAmount()).subtract(saleDTO.getDiscountAmount()));
        sale.setTaxAmount(saleDTO.getTaxAmount());
        sale.setDiscountAmount(saleDTO.getDiscountAmount());

        return saleRepository.save(sale);
    }

    private String generateTransactionCode() {
        return "TXN-" + System.currentTimeMillis();
    }

    /*public void processSale(Sale sale) {
        // Existing sale processing logic

        if(sale.getCustomer() != null) {
            customerService.updateTotalSpent(sale.getCustomer().getId(), sale.getTotalAmount());
            customerService.addLoyaltyPoints(
                    sale.getCustomer().getId(),
                    sale.getTotalAmount().intValue() // 1 point per currency unit
            );
        }
    }*/

   @Transactional
    public Sale processSale(Sale sale) {
        // Save the sale first
        Sale savedSale = saleRepository.save(sale);

        // Update customer metrics if customer is associated
        if(savedSale.getCustomer() != null) {
            try {
                customerService.updateTotalSpent(
                        savedSale.getCustomer().getId(),
                        savedSale.getTotalAmount()
                );
                customerService.addLoyaltyPoints(
                        savedSale.getCustomer().getId(),
                        calculateLoyaltyPoints(savedSale.getTotalAmount())
                );
            } catch (CustomerNotFoundException e) {
                // Handle missing customer (log error or throw exception)
                System.err.println("Customer not found: " + e.getMessage());
            }
        }
        return savedSale;
    }

    private int calculateLoyaltyPoints(BigDecimal amount) {
        // 1 point per dollar spent
        return amount.intValue();


    }

    /*public Map<String, BigDecimal> getDashboardSalesData() {
        LocalDate today = LocalDate.now();
        return Map.of(
                "daily", saleRepository.getTotalSalesByDate(today),
                "weekly", saleRepository.getTotalSalesBetweenDates(today.minusDays(7), today),
                "monthly", saleRepository.getTotalSalesBetweenDates(today.minusMonths(1), today)
        );
    }*/

    public Map<String, BigDecimal> getDashboardSalesData() {
        LocalDateTime now = LocalDateTime.now();
        return Map.of(
                "daily", saleRepository.getTotalSalesBetweenDates(
                        now.minusDays(1).with(LocalTime.MIN),
                        now.with(LocalTime.MAX)
                ),
                "weekly", saleRepository.getTotalSalesBetweenDates(
                        now.minusWeeks(1).with(LocalTime.MIN),
                        now.with(LocalTime.MAX)
                ),
                "monthly", saleRepository.getTotalSalesBetweenDates(
                        now.minusMonths(1).with(LocalTime.MIN),
                        now.with(LocalTime.MAX)
                )
        );
    }

    public Map<String, Object> getSalesTrends(String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch(period.toLowerCase()) {
            case "weekly" -> endDate.minusWeeks(1);
            case "monthly" -> endDate.minusMonths(1);
            case "yearly" -> endDate.minusYears(1);
            default -> endDate.minusDays(1);
        };

        Map<String, Object> response = new HashMap<>();
        response.put("labels", List.of(startDate.toString(), endDate.toString()));
        response.put("data", saleRepository.findSalesAmountsBetweenDates(startDate, endDate));
        return response;
    }
}


