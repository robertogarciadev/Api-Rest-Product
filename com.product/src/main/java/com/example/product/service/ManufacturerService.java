package com.example.product.service;

import com.example.product.model.Manufacturer;
import com.example.product.model.ManufacturerDTO;
import com.example.product.model.Product;
import com.example.product.repository.ManufacturerRepository;
import com.example.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerService {

    @Autowired
    ManufacturerRepository manufacturerRepository;
    @Autowired
    ProductRepository productRepository;

    //Crear un Fabricante
    public ManufacturerDTO create(ManufacturerDTO manufacturerDTO){

        //Obtiene el producto
        Optional<Product> productDb = (manufacturerDTO.getIdProduct()!=null)
                ? productRepository.findById(manufacturerDTO.getIdProduct())
                : Optional.empty();

        Manufacturer newManufacturer = manufacturerRepository.save(Manufacturer.builder()
                .name(manufacturerDTO.getName())
                .country(manufacturerDTO.getCountry())
                .product(productDb.orElse(null))
                .build());

        return ManufacturerDTO.builder()
                .id(newManufacturer.getId())
                .name(newManufacturer.getName())
                .country(newManufacturer.getCountry())
                .idProduct(
                        (newManufacturer.getProduct()!=null)? newManufacturer.getProduct().getId(): null
                )
                .build();
    }

    //Obtener todos los fabricantes
    public List<ManufacturerDTO> findAll(){
        List<Manufacturer> allManufacturer = manufacturerRepository.findAll();

        return allManufacturer.stream()
                .map(manufacturer ->
                        ManufacturerDTO.builder()
                        .id(manufacturer.getId())
                        .name(manufacturer.getName())
                        .country(manufacturer.getCountry())
                        .idProduct( (manufacturer.getProduct() == null) ? null: manufacturer.getProduct().getId()).build())
                .toList();

    }

    //Buscar fabricante por id
    public Optional<ManufacturerDTO> findById(Long id){

        Optional<Manufacturer> manufacturerDb = manufacturerRepository.findById(id);
        //El mapeo solo lo hace si el valor del optinal está presente, si no lo devuelve vacío.
        return manufacturerDb.map(manufacturer -> ManufacturerDTO.builder()
                .id(manufacturer.getId())
                .name(manufacturer.getName())
                .country(manufacturer.getCountry())
                .idProduct((manufacturer.getProduct() != null) ? manufacturer.getProduct().getId()
                        : null)
                .build());

    }

    //Actualizar un fabricante
    public Optional<ManufacturerDTO> update(ManufacturerDTO manufacturerDTO){

        //Busca el producto
        Optional<Product> product = (manufacturerDTO.getIdProduct() != null)
                ? productRepository.findById(manufacturerDTO.getIdProduct())
                : Optional.empty();
        //Busca al fabricante
        Optional<Manufacturer> manufacturerDb = (manufacturerDTO.getId()!=null)?
                manufacturerRepository.findById(manufacturerDTO.getId())
                :Optional.empty();

        //Actualiza al fabricante. Deja actualizar si existen el Producto y el fabricante
        if (manufacturerDb.isPresent()){
            manufacturerDb.get().setName(manufacturerDTO.getName());
            manufacturerDb.get().setCountry(manufacturerDTO.getCountry());
            manufacturerDb.get().setProduct(product.orElse(null));
            manufacturerRepository.save(manufacturerDb.get());
        }

        //Retorna el optional con el DTO
        return manufacturerDb.map(manufacturer->ManufacturerDTO.builder()
                .id(manufacturer.getId())
                .name(manufacturer.getName())
                .country(manufacturer.getCountry())
                .idProduct( (manufacturer.getProduct()!=null) ? manufacturer.getProduct().getId()
                        : null)
                .build());
    }


    //Borrar fabricante por id (Rompe la relación entre Producto y Fabricantes antes de borrar el fabricante)
    public void deleteById(Long id){
        List<Product> productWithSameManufacturer = productRepository.findByManufacturerId(id);
        productWithSameManufacturer.forEach(product -> product.setManufacturer(null));
        manufacturerRepository.deleteById(id);
    }

    //Borrar todos los fabricantes
    public void deleteAll(){
        List<Product> allProducts = productRepository.findAll();
        allProducts.forEach(product -> product.setManufacturer(null));
        manufacturerRepository.deleteAll();
    }

}
