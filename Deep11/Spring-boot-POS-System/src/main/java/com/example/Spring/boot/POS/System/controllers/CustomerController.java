package com.example.Spring.boot.POS.System.controllers;



import com.example.Spring.boot.POS.System.exception.CustomerNotFoundException;
import com.example.Spring.boot.POS.System.model.Customer;
import com.example.Spring.boot.POS.System.service.CustomerService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.searchCustomers(""));
        model.addAttribute("customer", new Customer());
        return "customers";
    }

    @PostMapping
    public String saveCustomer(@ModelAttribute Customer customer) {
        customerService.createCustomer(customer);
        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String editCustomerForm(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.getCustomerById(id));
        return "edit";
    }
/*
    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute Customer customer) {
        customerService.updateCustomer(id, customer);
        return "redirect:/customers";
    }*/

    /*@GetMapping("/search")
    public String searchCustomers(@RequestParam String query, Model model) {
        model.addAttribute("customers", customerService.searchCustomers(query));
        return "customers";
    }*/

    @GetMapping("/search")
    public String searchCustomers(@RequestParam String query, Model model) {
        List<Customer> results = customerService.searchCustomers(query);
        model.addAttribute("customers", results);
        model.addAttribute("searchQuery", query);

        if (results.isEmpty()) {
            model.addAttribute("noResults", true);
        }

        return "customers";
    }

    /*@GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }*/

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("success", "Customer deleted successfully");
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete customer with existing orders");
        }
        return "redirect:/customers";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Long id,
                                 @ModelAttribute Customer customer,
                                 RedirectAttributes redirectAttributes) {
        try {
            customerService.updateCustomer(id, customer);
            redirectAttributes.addFlashAttribute("success", "Customer updated successfully");
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/customers";
    }
}

