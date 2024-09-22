package com.example.product.controller;

import com.example.product.model.Manufacturer;
import com.example.product.model.ManufacturerDTO;
import com.example.product.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/manufacturer")
@RestController
public class ManufacturerController {

    @Autowired
    ManufacturerService manufacturerService;

    //Crear fabricante
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ManufacturerDTO manufacturerDTO){
        return ResponseEntity.ok(manufacturerService.create(manufacturerDTO));
    }

    //Obtener todos los fabricantes
    @GetMapping("/all")
    public ResponseEntity<List<ManufacturerDTO>> findAll(){
        return ResponseEntity.ok(manufacturerService.findAll());
    }

    //Obtener fabricante por id
    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerDTO> findById(@PathVariable Long id){
        Optional<ManufacturerDTO> manufacturer = manufacturerService.findById(id);
        return manufacturer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Actualizar fabricante
    @PutMapping("/update")
    public ResponseEntity<ManufacturerDTO> update(@RequestBody ManufacturerDTO manufacturerDTO){
        Optional<ManufacturerDTO> manufacturerDB = manufacturerService.update(manufacturerDTO);
        return manufacturerDB.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Borrar un fabricante
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){

        if (manufacturerService.findById(id).isPresent()){
            manufacturerService.deleteById(id);
            return ResponseEntity.accepted().build();
        }else
            return ResponseEntity.notFound().build();

    }

    //Borrar todos los fabricantes
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAll(){
        if (!manufacturerService.findAll().isEmpty()){
            manufacturerService.deleteAll();
            return ResponseEntity.accepted().build();
        }else
            return ResponseEntity.noContent().build();
    }
}
