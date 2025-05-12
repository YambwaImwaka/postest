package com.example.Spring.boot.POS.System.repository;



import com.example.Spring.boot.POS.System.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomerId(Long customerId);

    /*@Modifying
    @Query("DELETE FROM Address a WHERE a.customer.id = :customerId")
    void deleteByCustomerId(@Param("customerId") Long customerId);*/


        @Modifying
        @Transactional
        @Query("DELETE FROM Address a WHERE a.customer.id = :customerId")
        void deleteByCustomerId(@Param("customerId") Long customerId);

}
