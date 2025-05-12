package com.example.Spring.boot.POS.System.service;

import com.example.Spring.boot.POS.System.repository.AddressRepository;


import com.example.Spring.boot.POS.System.exception.CustomerNotFoundException;
import com.example.Spring.boot.POS.System.model.Customer;
import com.example.Spring.boot.POS.System.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.example.Spring.boot.POS.System.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public CustomerService(CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(customerDetails.getFirstName());
                    customer.setLastName(customerDetails.getLastName());
                    customer.setEmail(customerDetails.getEmail());
                    customer.setPhone(customerDetails.getPhone());
                    return customerRepository.save(customer);
                })
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    @Transactional
    public void addLoyaltyPoints(Long customerId, int points) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        customerRepository.save(customer);
    }

    @Transactional
    public void updateTotalSpent(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customer.setTotalSpent(customer.getTotalSpent().add(amount));
        customerRepository.save(customer);
    }

    public List<Customer> searchCustomers(String query) {
        return customerRepository.searchCustomers(query);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    /*@Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        // Handle associated addresses
        addressRepository.deleteByCustomerId(id);

        customerRepository.delete(customer);
    }*/

    @Transactional
    public void deleteCustomer(Long id) {
        // First delete addresses
        addressRepository.deleteByCustomerId(id);

        // Then delete customer
        customerRepository.deleteById(id);
    }

    /*public Map<String, Object> getCustomerStatistics() {
        return Map.of(
                "totalCustomers", customerRepository.count(),
                "newCustomersThisMonth", customerRepository.countNewCustomersThisMonth(),
                "topCustomers", customerRepository.findTopCustomersBySpending(5)
        );
    }*/

    public Map<String, Object> getCustomerStatistics() {
        LocalDateTime startOfMonth = LocalDateTime.now()
                .withDayOfMonth(1)
                .with(LocalTime.MIN);

        return Map.of(
                "totalCustomers", customerRepository.count(),
                "newCustomersThisMonth", customerRepository.countNewCustomersThisMonth(startOfMonth),
                "topCustomers", customerRepository.findTopCustomersBySpending(5)
        );
    }
}





