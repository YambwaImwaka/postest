package com.example.Spring.boot.POS.System.repository;



import com.example.Spring.boot.POS.System.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByLastNameContainingIgnoreCase(String lastName);

   /* @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Customer> searchCustomers(@Param("query") String query);*/



    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "c.phone LIKE CONCAT('%', :query, '%')")
    List<Customer> searchCustomers(@Param("query") String query);

    Customer findByEmail(String email);

    //@Query("SELECT COUNT(c) FROM Customer c WHERE c.createdAt >= :startOfMonth")
    //Long countNewCustomersThisMonth();

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.createdAt >= :startOfMonth")
    Long countNewCustomersThisMonth(@Param("startOfMonth") LocalDateTime startOfMonth);

    @Query("SELECT c FROM Customer c ORDER BY c.totalSpent DESC LIMIT :limit")
    List<Customer> findTopCustomersBySpending(@Param("limit") int limit);
}
