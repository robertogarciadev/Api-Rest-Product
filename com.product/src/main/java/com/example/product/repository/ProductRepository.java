package com.example.product.repository;

import com.example.product.model.Distributor;
import com.example.product.model.Manufacturer;
import com.example.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Optional<Product> findByNameIgnoreCase(String name);

//    @Query("SELECT * FROM products WHERE manufactured_id= ?1")
    List<Product> findByManufacturerId(Long idManufactured);

    List<Product> findByDistributorList(Distributor distributor);
    List<Product> findByCategoryId(Long idCategory);
}
