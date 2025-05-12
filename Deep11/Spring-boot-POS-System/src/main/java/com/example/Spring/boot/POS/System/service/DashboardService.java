package com.example.Spring.boot.POS.System.service;

import com.example.Spring.boot.POS.System.repository.SaleRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.example.Spring.boot.POS.System.repository.SaleRepository;
import com.example.Spring.boot.POS.System.model.Sale;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;


@Service
public class DashboardService {
    private final SaleRepository saleRepository;

    public DashboardService(SaleRepository salesRepository) {
        this.saleRepository = salesRepository;
    }

    public Map<String, Object> getSalesTrends(String period) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = calculateStartDate(period, endDate);

        return Map.of(
                "labels", getDateLabels(startDate, endDate),
                "data", processSalesData(
                        saleRepository.findSalesBetweenDates(
                                startDate.with(LocalTime.MIN),
                                endDate.with(LocalTime.MAX)
                        )
                )
        );
    }

    private LocalDateTime calculateStartDate(String period, LocalDateTime endDate) {
        return switch(period.toLowerCase()) {
            case "weekly" -> endDate.minusDays(7);
            case "monthly" -> endDate.minusMonths(1);
            case "yearly" -> endDate.minusYears(1);
            default -> endDate.minusDays(1);
        };
    }
    /*public Map<String, Object> getSalesTrends(String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = switch(period.toLowerCase()) {
            case "weekly" -> endDate.minusWeeks(1);
            case "monthly" -> endDate.minusMonths(1);
            case "yearly" -> endDate.minusYears(1);
            default -> endDate.minusDays(1); // Daily
        };

        return Map.of(
                "labels", getDateLabels(startDate.atStartOfDay(), endDate.atStartOfDay()),
                "data", saleRepository.findSalesBetweenDates(startDate.atStartOfDay(), endDate.atStartOfDay())
        );
    }*/

    /*private List<String> getDateLabels(LocalDate start, LocalDate end) {
        // Generate date labels based on period
        return List.of();
    }

    public Map<String, Object> getSalesTrends(String period) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = calculateStartDate(period, endDate);

        return Map.of(
                "labels", getDateLabels(startDate, endDate),
                "data", processSalesData(
                        saleRepository.findSalesBetweenDates(startDate, endDate)
                )
        );
    }*/

   /* private LocalDateTime calculateStartDate(String period, LocalDateTime endDate) {
        return switch(period.toLowerCase()) {
            case "weekly" -> endDate.minusDays(7);
            case "monthly" -> endDate.minusMonths(1);
            case "yearly" -> endDate.minusYears(1);
            default -> endDate.minusDays(1); // Daily
        };
    }*/

    private List<String> getDateLabels(LocalDateTime start, LocalDateTime end) {
        return saleRepository.findSalesBetweenDates(start, end)
                .stream()
                .map(sale -> sale.getCreatedAt().toLocalDate().toString())
                .distinct()
                .toList();
    }

    private List<BigDecimal> processSalesData(List<Sale> sales) {
        Map<LocalDate, BigDecimal> dailyTotals = new LinkedHashMap<>();

        sales.forEach(sale -> {
            LocalDate date = sale.getCreatedAt().toLocalDate();
            dailyTotals.merge(date, sale.getTotalAmount(), BigDecimal::add);
        });

        return new ArrayList<>(dailyTotals.values());
    }
}
