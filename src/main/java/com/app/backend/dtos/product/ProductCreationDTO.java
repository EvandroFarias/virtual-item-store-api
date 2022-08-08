package com.app.backend.dtos.product;


import com.app.backend.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCreationDTO {

    private String name;
    private Double price;
    private Double discount;

    public static Product dtoToModel(ProductCreationDTO dto){
        if(dto == null) return null;

        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .discount(dto.getDiscount())
                .build();
    }

}
