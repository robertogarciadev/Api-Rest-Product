package com.example.product.controller;

import com.example.product.model.Category;
import com.example.product.model.Product;
import com.example.product.model.ProductDTO;
import com.example.product.service.CategoryService;
import com.example.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    //Obtener todos los productos
    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> findAllProducts(){
        List<ProductDTO> allProductsDTO =  productService.findAllProducts();
        return !allProductsDTO.isEmpty() ? ResponseEntity.ok(allProductsDTO)
                        : ResponseEntity.noContent().build();
    }


    //Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        Optional<ProductDTO> productDb = productService.findProductById(id);
        return productDb.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    //Obtener producto por nombre
    @GetMapping("/byName")
    public ResponseEntity<ProductDTO> findByName(@RequestParam("name") String name){
        Optional<ProductDTO> productDb = productService.findProductByName(name);
        return productDb.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    //Ordenar productos por precios
    @GetMapping("/byPrice")
    public ResponseEntity<List<ProductDTO>> findAllByPrice(){
        List<ProductDTO> allProduct = productService.sortAllByPrice();
        return !allProduct.isEmpty() ? ResponseEntity.ok(allProduct): ResponseEntity.noContent().build();
    }


    //Crear producto
    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO){

       ProductDTO newProduct = productService.createProduct(productDTO);
       return ResponseEntity.ok(newProduct);

    }


    //Actualizar un producto
    @PutMapping("/update")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO){
        Optional<ProductDTO> productDbDTO = productService.updateProduct(productDTO);
        return productDbDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //Borrar producto
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        Optional<ProductDTO> productDb = productService.findProductById(id);

       if (productDb.isPresent()){
           productService.deleteProduct(id);
           return ResponseEntity.noContent().build();
       } else
           return ResponseEntity.notFound().build();
    }

    //Borrar todos los productos
    @DeleteMapping ("/deleteAll")
    public ResponseEntity<?> deleteAll(){
        productService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
