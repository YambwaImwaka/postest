package com.example.Spring.boot.POS.System.controllers;



import com.example.Spring.boot.POS.System.service.SalesService;
import com.example.Spring.boot.POS.System.service.InventoryService;
import com.example.Spring.boot.POS.System.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;



@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final SalesService salesService;
    private final InventoryService inventoryService;
    private final CustomerService customerService;

    public DashboardController(SalesService salesService,
                               InventoryService inventoryService,
                               CustomerService customerService) {
        this.salesService = salesService;
        this.inventoryService = inventoryService;
        this.customerService = customerService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("salesData", salesService.getDashboardSalesData());
        model.addAttribute("inventoryStatus", inventoryService.getInventoryStatus());
        model.addAttribute("customerStats", customerService.getCustomerStatistics());
        return "dashboard";
    }

    @GetMapping("/sales-data")
    @ResponseBody
    public Map<String, Object> getSalesData(@RequestParam String period) {
        return salesService.getSalesTrends(period);
    }
}
