package com.example.Spring.boot.POS.System.repository;

import com.example.Spring.boot.POS.System.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}
