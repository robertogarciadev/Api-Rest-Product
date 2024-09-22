package com.example.product.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManufacturerDTO {

    private Long id;
    private String name;
    private String country;
    @Nullable
    private Long idProduct;

}
