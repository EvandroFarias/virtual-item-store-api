package com.app.backend.services;

import com.app.backend.customException.NotActiveException;
import com.app.backend.customException.PendingTransactionException;
import com.app.backend.customException.RedundancyExcpetion;
import com.app.backend.dtos.transaction.TransactionCreationDTO;
import com.app.backend.dtos.transaction.TransactionViewDTO;
import com.app.backend.enums.TransactionStatus;
import com.app.backend.models.Product;
import com.app.backend.models.Transaction;
import com.app.backend.models.User;
import com.app.backend.repositories.ProductRepository;
import com.app.backend.repositories.TransactionRepository;
import com.app.backend.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    public TransactionViewDTO registerTransaction(TransactionCreationDTO transactionCreationDTO) throws Exception {

        try {
            User buyer = userRepository.findById(transactionCreationDTO.getBuyerId()).orElse(null);
            User seller = userRepository.findById(transactionCreationDTO.getSellerId()).orElse(null);
            Product product = productRepository.findById(transactionCreationDTO.getProductId()).orElse(null);

            this.isValidTransaction(buyer,seller,product);

            Transaction transaction = Transaction.builder()
                    .buyer(buyer)
                    .seller(seller)
                    .product(product)
                    .price(product.getPrice())
                    .status(TransactionStatus.PENDING)
                    .build();

            if(buyer.getBalance() < product.getPrice()){
                transaction.setStatus(TransactionStatus.REJECTED);
                transaction.setLastUpdateDate(new Date(System.currentTimeMillis()));
            }

            transactionRepository.save(transaction);
            return TransactionViewDTO.modelToDto(transaction);
        } catch (Exception e){
            log.error("Error creating transaction at Transaction Service");
            e.printStackTrace();
            throw e;
        }

    }

    private void isValidTransaction(User buyer, User seller, Product product) throws Exception{
        if (buyer == null
                || seller == null
                || product == null){
            String message = "Invalid ";
            message += buyer == null ? "buyer " : "";
            message += seller == null ? "seller " : "";
            message += product == null ? "product" : "";
            throw new NoSuchElementException("Operation invalid. " + message);
        }
        if(!buyer.getIsActive() || !seller.getIsActive()){
            throw new NotActiveException("Buyer or Seller is not active");
        }
        Optional<Transaction> toValidate = transactionRepository.findByProductId(product.getId());
        if(toValidate.isPresent() && toValidate.get().getStatus() == TransactionStatus.PENDING){
            throw new PendingTransactionException("There is a pending transaction for this product already");
        }
        if(product.getUser() != seller){
            throw new Exception("Product seller is incorrect");
        }
        if(buyer == seller){
            throw new RedundancyExcpetion("Buyer and seller are the same user");
        }
    }
}
