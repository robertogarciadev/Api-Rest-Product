package com.example.product.controller;

import com.example.product.model.Category;
import com.example.product.model.CategoryDTO;
import com.example.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    

    //Obtener todas las categorías
    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> findAllCategory(){
        List<CategoryDTO> allCategory = categoryService.findAll();
        return !allCategory.isEmpty() ? ResponseEntity.ok(allCategory):ResponseEntity.noContent().build();
    }

    //Obtener categoría por id
    @GetMapping("{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
        Optional<CategoryDTO> categoryDb = categoryService.findById(id);
        return categoryDb.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Crear Categoría
    @PostMapping ("/create")
    public ResponseEntity<Category> create(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.create(category));
    }

    //Actualizar categoría
    @PutMapping("/update")
    public ResponseEntity<CategoryDTO> update(@RequestBody Category category){

        Optional<CategoryDTO> categoryDb = categoryService.update(category);
        return categoryDb.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    //Borrar categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){

        if (categoryService.findById(id).isPresent()){
            categoryService.deleteById(id);
            return ResponseEntity.accepted().build();
        } else
            return ResponseEntity.notFound().build();

    }

    //Borrar todas las categorías
    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAll(){

        if(!categoryService.findAll().isEmpty()){
            categoryService.deleteAll();
            return ResponseEntity.accepted().build();
        }else
            return ResponseEntity.noContent().build();
    }
}
