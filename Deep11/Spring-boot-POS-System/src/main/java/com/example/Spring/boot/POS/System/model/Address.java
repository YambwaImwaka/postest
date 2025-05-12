package com.example.Spring.boot.POS.System.model;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
   @JsonBackReference
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;

    private String street;
    private String city;
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    private String country;

    public enum AddressType {
        HOME, WORK, OTHER
    }

   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customer_id",
            foreignKey = @ForeignKey(
                    name = "fk_customer_address",
                    foreignKeyDefinition = "FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE"
            )
    )
    private Customer customer;*/

    // Other fields and methods
}