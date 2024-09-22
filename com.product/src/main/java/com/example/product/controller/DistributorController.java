package com.example.product.controller;

import com.example.product.model.Distributor;
import com.example.product.model.DistributorDTO;
import com.example.product.service.DistributorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/distributor")
@Slf4j
public class DistributorController {

    @Autowired
    DistributorService distributorService;

    // Crear-Guardar un nuevo distribuidor
    @PostMapping(value ="/create", consumes = "application/json")
    public ResponseEntity<?> createDistributor(@RequestBody Distributor distributor){
        DistributorDTO newDistributorDTO = distributorService.create(distributor);
        log.info("Petición exitosa");
        return ResponseEntity.ok(newDistributorDTO);
    }

    // Obtener todos los distribuidores
    @GetMapping("/all")
    public ResponseEntity<?> findAllDistributor(){

        return !distributorService.allDistributor().isEmpty()
                ? ResponseEntity.ok(distributorService.allDistributor())
                : ResponseEntity.noContent().build();
    }

    //Buscar por distribuidor por Id
    @GetMapping("/{id}")
    public ResponseEntity<DistributorDTO> findById(@PathVariable Long id){
        Optional<DistributorDTO> distributorDbDTO = distributorService.findById(id);
        return distributorDbDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //Actualizar distribuidor
    @PutMapping("/update")
    public ResponseEntity<DistributorDTO> update(@RequestBody Distributor distributor){

        Optional<DistributorDTO> distributorDTO = distributorService.update(distributor);

        return distributorDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //Borrar distribuidor por Id
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteById(@RequestParam Long id){

        Optional<Distributor> distributorToDelete = distributorService.findDistributorById(id);
        if (distributorToDelete.isPresent()){
            distributorService.deleteById(id);
            return ResponseEntity.accepted().build();
        } else return ResponseEntity.notFound().build();
    }
}
