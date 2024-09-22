package com.example.product.service;

import com.example.product.model.*;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    //Obtener todas las categorías
    public List<CategoryDTO> findAll(){
        List<Category> allCategory = categoryRepository.findAll();

        return allCategory.stream()
                .map(category -> CategoryDTO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .productList(
                                category.getProductList().stream()
                                        .map(product -> ProductDTO.builder()
                                                .id(product.getId())
                                                .name(product.getName())
                                                .price(product.getPrice())
                                                .idCategory(product.getCategory().getId())
                                                .idManufacturer(
                                                        (product.getManufacturer() != null) ?
                                                                product.getManufacturer().getId() : null
                                                )
                                                .distributors(
                                                        product.getDistributorList().stream()
                                                                .mapToLong(Distributor::getId)
                                                                .boxed().toList()
                                                )
                                                .build())
                                        .toList()
                        )
                        .build())
                .toList();
    }

    //Obtener categoría por id
    public Optional<CategoryDTO> findById(Long id){
        Optional<Category> categoryDb = categoryRepository.findById(id);

        return categoryDb.map(category -> CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .productList(
                        (category.getProductList()!=null) ?
                                category.getProductList().stream()
                                        .map(product -> ProductDTO.builder()
                                                .id(product.getId())
                                                .name(product.getName())
                                                .price(product.getPrice())
                                                .idCategory(product.getCategory().getId())
                                                .idManufacturer(
                                                        (product.getManufacturer()!=null) ?
                                                                product.getManufacturer().getId() : null
                                                )
                                                .distributors(
                                                        (!product.getDistributorList().isEmpty()) ?
                                                                product.getDistributorList().stream()
                                                                        .mapToLong(Distributor::getId)
                                                                        .boxed().toList() : List.of()
                                                )
                                                .build())
                                        .toList() : null
                )
                .build());
    }

    //Crear categoría
    public Category create(Category category){
        return categoryRepository.save(category);
    }



    //Actualizar nombre categoría
    public Optional<CategoryDTO> update(Category category){
        Optional<Category> categoryDb = (category.getId()!=null)?
                categoryRepository.findById(category.getId())
                :Optional.empty();

        if (categoryDb.isPresent()){
          categoryDb.get().setName(category.getName());
          categoryRepository.save(categoryDb.get());
        }

        return categoryDb
                .map(category1 -> CategoryDTO.builder()
                        .id(category1.getId())
                        .name(category1.getName())
                        .productList(
                                category1.getProductList().stream()
                                        .map(product -> ProductDTO.builder()
                                                .id(product.getId())
                                                .name(product.getName())
                                                .price(product.getPrice())
                                                .idCategory(product.getCategory().getId())
                                                .idManufacturer(
                                                        (product.getManufacturer() != null) ?
                                                                product.getManufacturer().getId() : null
                                                )
                                                .distributors(
                                                        product.getDistributorList().stream()
                                                                .mapToLong(Distributor::getId)
                                                                .boxed().toList()
                                                )
                                                .build())
                                        .toList()
                        )
                        .build());
    }

    //Borrar categoría
    public void deleteById(Long id){

        List<Product> productsWithSameCategory = productRepository.findByCategoryId(id);
        productsWithSameCategory.forEach(product -> product.setCategory(null));
        categoryRepository.deleteById(id);
    }

    //Borrar todas las categorías
    public void deleteAll(){

        List<Product> allProducts = productRepository.findAll();
        allProducts.forEach(product -> product.setCategory(null));
        categoryRepository.deleteAll();
    }
}
