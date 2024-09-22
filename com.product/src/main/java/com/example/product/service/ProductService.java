package com.example.product.service;

import com.example.product.model.*;

import com.example.product.repository.CategoryRepository;
import com.example.product.repository.DistributorRepository;
import com.example.product.repository.ManufacturerRepository;
import com.example.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ManufacturerRepository manufacturerRepository;
    @Autowired
    DistributorRepository distributorRepository;


    //Obtener todos los productos
    public List<ProductDTO> findAllProducts(){

        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .idCategory(
                                (product.getCategory() != null) ? product.getCategory().getId() : null
                        )
                        .idManufacturer(
                                (product.getManufacturer() != null) ? product.getManufacturer().getId() : null
                        )
                        .distributors(
                                product.getDistributorList().stream()
                                        .mapToLong(Distributor::getId)
                                        .boxed()
                                        .toList()
                        )
                        .build())
                .toList();
    }

    //Buscar un producto  por ID
    public Optional<ProductDTO> findProductById(Long id){
        Optional<Product> productDb =  productRepository.findById(id);

        return productDb.map(product -> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .idCategory(
                        (product.getCategory() != null) ? product.getCategory().getId() : null
                )
                .idManufacturer(
                        (product.getManufacturer() != null) ? product.getManufacturer().getId() : null
                )
                .distributors(
                        (product.getDistributorList() != null) ?
                                product.getDistributorList().stream()
                                        .map(Distributor::getId).toList() : List.of()
                )
                .build());
    }

    //Buscar por nombre
    public Optional<ProductDTO> findProductByName(String name){
        Optional<Product> productDb = productRepository.findByNameIgnoreCase(name);

        return productDb.map(product -> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .idCategory(
                        (product.getCategory() != null) ? product.getCategory().getId() : null
                )
                .idManufacturer(
                        (product.getManufacturer()!=null)? product.getManufacturer().getId() : null
                )
                .distributors(
                        (product.getDistributorList() != null) ?
                                product.getDistributorList().stream()
                                        .map(Distributor::getId).toList() : List.of()
                )
                .build());
    }

    //Listar productos por precio
    public List<ProductDTO> sortAllByPrice(){
        List<Product> allProduct = productRepository.findAll(Sort.by("price").descending());

        return allProduct.stream()
                .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .idCategory(
                                (product.getCategory()!=null)?product.getCategory().getId(): null
                        )
                        .idManufacturer(
                                (product.getManufacturer()!=null)? product.getManufacturer().getId():null
                        )
                        .distributors(
                                (!product.getDistributorList().isEmpty()) ?
                                        product.getDistributorList().stream()
                                                .map(Distributor::getId).toList() : List.of()
                        )
                        .build())
                .toList();
    }


    //CREATE NEW PRODUCT
    public ProductDTO createProduct(ProductDTO productDTO){

        //Find category
        Optional<Category> category = (productDTO.getIdCategory() != null)
                ? categoryRepository.findById(productDTO.getIdCategory())
                : Optional.empty();
        //Find mnufacturer
       Optional<Manufacturer> manufacturer = (productDTO.getIdManufacturer() != null)
               ? manufacturerRepository.findById(productDTO.getIdManufacturer())
               : Optional.empty();

       //Find distributors list
        List<Distributor> distributorList = productDTO.getDistributors().stream()
                .map(id-> distributorRepository.findById(id))
                .map(optional-> optional.orElse(null))
                .filter(Objects::nonNull)
                    .toList();


        //Save product en repository
        Product newProduct = productRepository.save(Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .category(category.orElse(null))
                .manufacturer(manufacturer.orElse(null))
                .distributorList(distributorList)
                .build());

        //Return DTO of product saved
        return ProductDTO.builder()
                .id(newProduct.getId())
                .name(newProduct.getName())
                .price(newProduct.getPrice())
                .idCategory(newProduct.getCategory()!=null ? newProduct.getCategory().getId():null)
                .idManufacturer(newProduct.getManufacturer() != null ? newProduct.getManufacturer().getId():null)
                .distributors(
                        (newProduct.getDistributorList() != null)
                                ? newProduct.getDistributorList().stream()
                                .filter(Objects::nonNull)
                                .mapToLong(Distributor::getId)
                                .boxed().toList() : List.of()
                )
                .build();
    }

    //UPDATE PRODUCT
    public Optional<ProductDTO> updateProduct(ProductDTO productDTO) {
        //Busca el producto en la base de datos
        Optional<Product> productDb = (productDTO.getId()!=null) ?
                productRepository.findById(productDTO.getId())
                :Optional.empty();

        //Busco la categoría en la base de datos
        Optional<Category> category = (productDTO.getIdCategory() != null)
                ? categoryRepository.findById(productDTO.getIdCategory())
                : Optional.empty();
        //Buscar fabricante
        Optional<Manufacturer> manufacturer = (productDTO.getIdManufacturer() != null)
                ? manufacturerRepository.findById(productDTO.getIdManufacturer())
                : Optional.empty();

        //Buscas distribuidores
        List<Distributor> distributorListMutable;
        if (productDb.isPresent()){
            distributorListMutable = new ArrayList<>(productDb.get().getDistributorList());
            distributorListMutable.clear();
            distributorListMutable.addAll(productDTO.getDistributors().stream()
                    .map(id -> distributorRepository.findById(id))
                    .map(optional -> optional.orElse(null))
                    .filter(Objects::nonNull)
                    .toList());
        } else {
            distributorListMutable = null;
        }


        //Actualiza el producto
        productDb.ifPresent(product -> {
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setCategory(category.orElse(null));
            product.setManufacturer(manufacturer.orElse(null));
            product.setDistributorList(distributorListMutable);
            productRepository.save(productDb.get());

        });
        //retorna el ProductDTO
        return productDb.map(product-> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .idCategory(
                        (product.getCategory()!=null) ? product.getCategory().getId() : null
                )
                .idManufacturer(
                        (product.getManufacturer()!=null) ? product.getManufacturer().getId() : null
                )
                .distributors(
                        (product.getDistributorList()!=null) ?
                                product.getDistributorList().stream()
                                        .map(Distributor::getId).toList() : List.of()
                )
                .build());
    }

    //Borrar Producto
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

    //Borrar todos los productos
    public void deleteAll(){
        productRepository.deleteAll();
    }
}
