package com.example.product.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private Double price;

    @Column(length = 50)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @OneToOne
    @JoinColumn(name = "manufactured_id")
    @Nullable
    private Manufacturer manufacturer;

    @ManyToMany
    @JoinTable (name = "product_distributor",
    joinColumns = @JoinColumn(name = "id_product"),
    inverseJoinColumns = @JoinColumn(name = "id_distributor"))
    private List<Distributor> distributorList;



}
