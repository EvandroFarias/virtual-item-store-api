package com.app.backend.dtos.transaction;

import com.app.backend.models.Product;
import com.app.backend.models.Transaction;
import com.app.backend.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionCreationDTO {
    private UUID buyerId;
    private UUID sellerId;
    private UUID productId;

}
