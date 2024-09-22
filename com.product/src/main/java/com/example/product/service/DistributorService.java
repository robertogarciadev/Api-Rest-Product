package com.example.product.service;

import com.example.product.model.Distributor;
import com.example.product.model.DistributorDTO;
import com.example.product.model.Product;
import com.example.product.model.ProductDTO;
import com.example.product.repository.DistributorRepository;
import com.example.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DistributorService {

    @Autowired
    DistributorRepository distributorRepository;

    @Autowired
    ProductRepository productRepository;

    //Crear un Distribuidor
    public DistributorDTO create(Distributor distributor){
        Distributor newDistributor = distributorRepository.save(distributor);

        return DistributorDTO.builder()
                .id(newDistributor.getId())
                .name(newDistributor.getName())
                .address(newDistributor.getAddress())
                .phone(newDistributor.getPhone())
                .email(newDistributor.getEmail())
                .productList(newDistributor.getProductList().stream()
                        .map(product -> ProductDTO.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .price(product.getPrice())
                                .idCategory(
                                        (product.getCategory()!=null) ? product.getCategory().getId() :null)
                                .idManufacturer(
                                        (product.getManufacturer()!=null) ? product.getManufacturer().getId() :null)
                                .build()).toList())
                .build();
    }

    //Obtener todos los distribuidores
    public List<DistributorDTO> allDistributor(){
        List<Distributor> allDistributor = distributorRepository.findAll();

        return allDistributor.stream()
                .map(distributor -> DistributorDTO.builder()
                        .id(distributor.getId())
                        .name(distributor.getName())
                        .address(distributor.getAddress())
                        .phone(distributor.getPhone())
                        .email(distributor.getEmail())
                        .productList(
                                distributor.getProductList().stream()
                                        .map(product -> ProductDTO.builder()
                                                .id(product.getId())
                                                .name(product.getName())
                                                .price(product.getPrice())
                                                .idCategory( (product.getCategory() != null) ? product.getCategory().getId(): null )
                                                .idManufacturer( (product.getManufacturer()!=null) ? product.getManufacturer().getId(): null )
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

    //Buscar distribuidor por Id. Devuelve DTO
    public Optional<DistributorDTO> findById(Long id){
        Optional<Distributor> distributorDb = distributorRepository.findById(id);

        return distributorDb.map(distributor -> DistributorDTO.builder()
                .id(distributor.getId())
                .name(distributor.getName())
                .address(distributor.getAddress())
                .phone(distributor.getPhone())
                .email(distributor.getEmail())
                .productList(distributor.getProductList().stream()
                        .map(product -> ProductDTO.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .price(product.getPrice())
                                .idCategory( (product.getCategory()!=null) ? product.getCategory().getId() : null )
                                .idManufacturer((product.getManufacturer()!=null)? product.getManufacturer().getId():null)
                                .distributors( product.getDistributorList().stream()
                                        .mapToLong(Distributor::getId)
                                        .boxed().toList())
                                .build()).toList())
                .build());
    }

    //Buscar distribuidor por Id. Devuelve Distributor
    public Optional<Distributor> findDistributorById(Long id){
        return distributorRepository.findById(id);
    }

    //actualizar distribuidor
    public Optional<DistributorDTO> update(Distributor distributor){

        Optional<Distributor> distributorDb = (distributor.getId()!=null) ?
                distributorRepository.findById(distributor.getId())
                :Optional.empty();

        if (distributorDb.isPresent()){
            distributorDb.get().setName(distributor.getName());
            distributorDb.get().setAddress(distributor.getAddress());
            distributorDb.get().setPhone(distributor.getPhone());
            distributorDb.get().setEmail(distributor.getEmail());
            distributorDb.get().setProductList(
                    (distributor.getProductList()!=null) ? distributor.getProductList() : null
            );
            distributorRepository.save(distributorDb.get());
        }

        return distributorDb.map(distributor1-> DistributorDTO.builder()
                .id(distributor1.getId())
                .name(distributor1.getName())
                .address(distributor1.getAddress())
                .phone(distributor1.getPhone())
                .email(distributor1.getEmail())
                .productList(
                        distributor1.getProductList().stream()
                                .map(product -> ProductDTO.builder()
                                        .id(product.getId())
                                        .name(product.getName())
                                        .price(product.getPrice())
                                        .idCategory(
                                                (product.getCategory()!=null) ? product.getCategory().getId():null
                                        )
                                        .idManufacturer(
                                                (product.getManufacturer()!=null)? product.getManufacturer().getId():null
                                        )
                                        .distributors(
                                                (product.getDistributorList() != null)
                                                        ? product.getDistributorList().stream()
                                                        .mapToLong(Distributor::getId).boxed().toList() : null
                                        )
                                        .build())
                                .toList())
                .build());
    }

    //Borrar distribuidor por Id
    public void deleteById(Long id){

        Optional<Distributor> distributorDb = distributorRepository.findById(id);
        if (distributorDb.isPresent()){
           List<Product> productsWithSameDistributor =
                   productRepository.findByDistributorList(distributorDb.get());
           productsWithSameDistributor.forEach(product -> product.setDistributorList(null));
            distributorRepository.deleteById(id);
        }
    }

}
