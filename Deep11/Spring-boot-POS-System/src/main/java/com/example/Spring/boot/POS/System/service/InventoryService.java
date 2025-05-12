package com.example.Spring.boot.POS.System.service;

import com.example.Spring.boot.POS.System.model.Product;
import com.example.Spring.boot.POS.System.model.PurchaseOrder;
import com.example.Spring.boot.POS.System.model.PurchaseOrderItem;
import com.example.Spring.boot.POS.System.repository.ProductRepository;
import com.example.Spring.boot.POS.System.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import com.example.Spring.boot.POS.System.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;


@Service
public class InventoryService {
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public InventoryService(ProductRepository productRepository,
                            PurchaseOrderRepository purchaseOrderRepository) {
        this.productRepository = productRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateStock(Long productId, int quantityChange) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        int newQuantity = product.getStockQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        //product.setStockQuantity(newQuantity);
        //checkLowStock(product);

        product.setStockQuantity(product.getStockQuantity() + quantityChange);
        checkLowStock(product);
        return productRepository.save(product);
    }

    private void checkLowStock(Product product) {
        if (product.getStockQuantity() < product.getReorderLevel()) {
            generatePurchaseOrder(product);
        }
    }

    private void generatePurchaseOrder(Product product) {
        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNumber("PO-" + System.currentTimeMillis());
        order.setOrderDate(LocalDate.now());
        order.setExpectedDeliveryDate(LocalDate.now().plusDays(7));
        order.setStatus("PENDING");
        order.setSupplier(product.getSupplier());

        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setProduct(product);
        item.setQuantityOrdered(product.getReorderLevel() * 2); // Order double the reorder level
        item.setQuantityReceived(0);

        order.getItems().add(item);
        purchaseOrderRepository.save(order);
    }

    @Transactional
    public void receivePurchaseOrder(Long orderId) {
        PurchaseOrder order = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        for (PurchaseOrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantityReceived());
            productRepository.save(product);
        }

        order.setStatus("RECEIVED");
        purchaseOrderRepository.save(order);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findByStockQuantityLessThan(10);
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product has associated sales or purchase orders
        if (!product.getSaleItems().isEmpty() || !product.getPurchaseOrderItems().isEmpty()) {
            throw new IllegalStateException("Cannot delete product with existing transactions");
        }

        productRepository.delete(product);
    }

   /* public Map<String, Object> getInventoryStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("totalProducts", productRepository.count());
        status.put("lowStock", productRepository.findByStockQuantityLessThan(10));
        status.put("outOfStock", productRepository.findByStockQuantity(0));
        status.put("totalValue", productRepository.calculateTotalInventoryValue());
        return status;
    }*/

    public Map<String, Object> getInventoryStatus() {
        Map<String, Object> status = new LinkedHashMap<>();

        status.put("totalProducts", productRepository.count());
        status.put("lowStock", productRepository.findByStockQuantityLessThan(10));
        status.put("outOfStock", productRepository.findByStockQuantity(0));
        status.put("totalValue", productRepository.calculateTotalInventoryValue());

        return status;
    }

}