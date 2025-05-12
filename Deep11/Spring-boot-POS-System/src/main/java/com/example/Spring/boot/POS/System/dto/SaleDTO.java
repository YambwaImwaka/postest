package com.example.Spring.boot.POS.System.dto;

import com.example.Spring.boot.POS.System.model.Sale;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//public class SaleDTO {
    /*private String paymentMethod;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private List<SaleItemDTO> items;*/


    /*@NotEmpty(message = "Payment method is required")
    private String paymentMethod;

    @DecimalMin(value = "0.0", message = "Tax amount cannot be negative")
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @NotEmpty(message = "At least one item is required")
    private List<SaleItemDTO> items;


    // Getters and setters
    public Sale.PaymentMethod getPaymentMethod() {
        return Sale.PaymentMethod.valueOf(paymentMethod);
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public List<SaleItemDTO> getItems() {
        return items;
    }

    public void setItems(List<SaleItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getAmount() {
        BigDecimal amount = null;
        return amount;
    }

}*/



/*public class SaleItemDTO {
    private Long productId;
    private int quantity;

    // Getters and setters
}*/

public class SaleDTO {
    //private String paymentMethod;
    //private BigDecimal taxAmount;
    //private BigDecimal discountAmount;
   // private List<SaleItemDTO> items;

    @NotEmpty(message = "Payment method is required")
    private String paymentMethod;

    @DecimalMin(value = "0.0", message = "Tax amount cannot be negative")
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @NotEmpty(message = "At least one item is required")
    private List<SaleItemDTO> items;

    //private List<SaleItemDTO> items;

    // Constructor
    public SaleDTO() {
        this.items = new ArrayList<>(); // Initialize the list
    }



    // Getters and setters
    //public Sale.PaymentMethod getPaymentMethod() {
      // return Sale.PaymentMethod.valueOf(paymentMethod);
  // }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }


    public List<SaleItemDTO> getItems() {
        return items;
    }

    public void setItems(List<SaleItemDTO> items) {
        this.items = items;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }
}

