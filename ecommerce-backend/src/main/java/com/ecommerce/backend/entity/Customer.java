package com.ecommerce.backend.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="customers")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="mobile_number")
    @NotBlank(message = "Phone number is required")
    @Size(min = 10,max = 15,message = "Phone must be 10-15 digits")
    private String mobileNo;

    @Column(name="address",nullable = false)
    @Size(min = 3, max = 50 , message = "Address must be 3-50 characters")
    @NotBlank(message = "Address is required")
    private String address;

    @Column(name="street_name")
    private String streetName;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}
