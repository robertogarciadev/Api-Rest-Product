package com.example.product.model;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    @NotNull
    private String name;
    @Min(value = 0)
    private Double price;
    @Min(value = 1)
    private Long idCategory;
    @Min(value = 1)
    private Long idManufacturer;
    private List<Long> distributors;
}
