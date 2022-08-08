package com.app.backend.dtos.transaction;

import com.app.backend.dtos.product.ProductViewDTO;
import com.app.backend.dtos.user.UserViewDTO;
import com.app.backend.enums.TransactionStatus;
import com.app.backend.models.Transaction;
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
public class TransactionViewDTO {
    private UUID id;
    private UserViewDTO buyer;
    private UserViewDTO seller;
    private ProductViewDTO product;
    private TransactionStatus status;
    private Date createdAt;
    private Date lastUpdateDate;

    public static TransactionViewDTO modelToDto(Transaction transaction){
        return TransactionViewDTO.builder()
                .id(transaction.getId())
                .buyer(UserViewDTO.modelToDto(transaction.getBuyer()))
                .seller(UserViewDTO.modelToDto(transaction.getSeller()))
                .product(ProductViewDTO.modelToDto(transaction.getProduct()))
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .lastUpdateDate(transaction.getLastUpdateDate())
                .build();
    }
}
