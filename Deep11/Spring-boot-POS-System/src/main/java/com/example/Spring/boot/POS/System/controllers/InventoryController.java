package com.example.Spring.boot.POS.System.controllers;

import com.example.Spring.boot.POS.System.model.Product;
import com.example.Spring.boot.POS.System.service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public String inventoryDashboard(Model model) {
        model.addAttribute("products", inventoryService.getAllProducts());
        model.addAttribute("lowStockProducts", inventoryService.getLowStockProducts());
        model.addAttribute("newProduct", new Product());

        return "inventory";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute Product product) {
        inventoryService.createProduct(product);
        return "redirect:/inventory";

    }

    /*@PostMapping("/adjust-stock")
    public String adjustStock(@RequestParam Long productId,
                              @RequestParam int adjustment) {
        inventoryService.updateStock(productId, adjustment);
        return "redirect:/inventory";
    }*/

    @GetMapping("/purchase-orders")
    public String viewPurchaseOrders(Model model) {
        model.addAttribute("orders", inventoryService.getAllPurchaseOrders());
        return "inventory/purchase-orders";
    }

    @PostMapping("/receive-order/{orderId}")
    public String receiveOrder(@PathVariable Long orderId) {
        inventoryService.receivePurchaseOrder(orderId);
        return "redirect:/inventory/purchase-orders";
    }

    /*@PostMapping("/products")
    public String createProduct(@Valid @ModelAttribute("newProduct") Product product,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", getAllProducts());
            model.addAttribute("lowStockProducts", getLowStockProducts());
            return "inventory/dashboard";
        }

        // Check for duplicate SKU
        if (ProductRepository.existsBySku(product.getSku())) {
            result.rejectValue("sku", "error.product", "SKU already exists");
            model.addAttribute("products", getAllProducts());
            model.addAttribute("lowStockProducts", getLowStockProducts());
            return "inventory/dashboard";
        }

        inventoryService.createProduct(product);
        return "redirect:/inventory";
    }*/

    @PostMapping("/adjust-stock")
    public String adjustStock(
            @RequestParam Long productId,
            @RequestParam String adjustmentType,
            @RequestParam int quantity,
            RedirectAttributes redirectAttributes) {

        try {
            int adjustment = adjustmentType.equals("ADD") ? quantity : -quantity;
            inventoryService.updateStock(productId, adjustment);
            redirectAttributes.addFlashAttribute("success", "Stock updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating stock: " + e.getMessage());
        }
        return "redirect:/inventory";
    }

    @PostMapping("/products/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        try {
            inventoryService.deleteProduct(productId);
            redirectAttributes.addFlashAttribute("success", "Product deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/inventory";
    }


}