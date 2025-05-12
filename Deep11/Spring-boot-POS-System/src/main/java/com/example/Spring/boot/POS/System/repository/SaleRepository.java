package com.example.Spring.boot.POS.System.repository;

import com.example.Spring.boot.POS.System.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


import com.example.Spring.boot.POS.System.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

   // @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE DATE(s.createdAt) = :date")
   //BigDecimal getTotalSalesByDate(@Param("date") LocalDate date);

    //@Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.createdAt BETWEEN :start AND :end")
    //BigDecimal getTotalSalesBetweenDates(@Param("start") LocalDate start,
                                   //      @Param("end") LocalDate end);

    @Query("SELECT DATE(s.createdAt) as date, SUM(s.totalAmount) as amount " +
            "FROM Sale s WHERE s.createdAt BETWEEN :start AND :end " +
            "GROUP BY DATE(s.createdAt)")
    List<Object[]> findSalesAmountsBetweenDates(@Param("start") LocalDate start,
                                                @Param("end") LocalDate end);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.createdAt BETWEEN :start AND :end")
    BigDecimal getTotalSalesBetweenDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

   /* @Query("SELECT DATE(s.createdAt) as date, SUM(s.totalAmount) as amount " +
            "FROM Sale s WHERE s.createdAt BETWEEN :start AND :end " +
            "GROUP BY DATE(s.createdAt)")
    List<Object[]> findSalesAmountsBetweenDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );*/

    @Query("SELECT s FROM Sale s WHERE s.createdAt BETWEEN :startDate AND :endDate ORDER BY s.createdAt")
    List<Sale> findSalesBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query("SELECT s FROM Sale s ORDER BY s.createdAt DESC LIMIT 5")
    List<Sale> findRecentSales();

    Optional<Sale> findByTransactionCode(String transactionCode);
}
