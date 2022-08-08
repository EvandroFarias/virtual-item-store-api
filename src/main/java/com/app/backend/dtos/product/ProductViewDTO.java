package com.app.backend.dtos.product;

import com.app.backend.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductViewDTO {

    private UUID id;
    private String name;
    private Double price;
    private Double discount;
    private Date createdAt;
    private Date lastUpdateDate;

    public static ProductViewDTO modelToDto(Product product){
        if(product == null) return null;

        return ProductViewDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .createdAt(product.getCreatedAt())
                .lastUpdateDate(product.getCreatedAt())
                .build();
    }
}
