package com.app.backend.services;

import com.app.backend.customException.NotActiveException;
import com.app.backend.dtos.product.ProductViewDTO;
import com.app.backend.models.Product;
import com.app.backend.models.User;
import com.app.backend.repositories.ProductRepository;
import com.app.backend.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public ProductViewDTO create(Product product, UUID userId) throws Exception {
        try {
            User user = userRepository.findById(userId).orElse(null);

            if (user == null) {
                throw new NoSuchElementException("Error on finding user");
            }
            if (!user.getIsActive()) {
                throw new NotActiveException("User is not active");
            }

            product.setUser(user);
            productRepository.save(product);

            return ProductViewDTO.modelToDto(product);
        } catch (Exception e) {
            log.error("Error creating product at Product Service");
            e.printStackTrace();
            throw e;
        }
    }


    public List<ProductViewDTO> getAll() {
        try {
            List<ProductViewDTO> productList = new ArrayList<ProductViewDTO>();
            productRepository.findByIsActiveTrue()
                    .forEach(product -> productList.add(ProductViewDTO.modelToDto(product)));

            return productList;
        } catch (Exception e) {
            log.error("Error listing all products at Product Service");
            e.printStackTrace();
            throw e;
        }
    }

    public List<ProductViewDTO> findByUser(UUID userId) throws Exception {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) throw new Exception("User not found");
            List<ProductViewDTO> productList = new ArrayList<ProductViewDTO>();

            productRepository.findByUserAndIsActiveTrue(user)
                    .forEach(product -> productList.add(ProductViewDTO.modelToDto(product)));

            return productList;
        } catch (Exception e) {
            log.error("Error listing all products by user id at Product Service");
            e.printStackTrace();
            throw e;
        }

    }

    public ProductViewDTO update(UUID productId, Product tempProduct) throws Exception {
        try {
            Product product = productRepository.findById(productId)
                    .orElse(null);

            this.isValid(product, tempProduct);

            product.setPrice(tempProduct.getPrice());
            product.setDiscount(tempProduct.getDiscount());
            product.setLastUpdateDate(new Date(System.currentTimeMillis()));
            productRepository.save(product);

            return ProductViewDTO.modelToDto(product);

        } catch (Exception e) {
            log.error("Error updating product at Product Service");
            e.printStackTrace();
            throw e;
        }

    }

    public ProductViewDTO delete(UUID productId) throws Exception {
        try {
            Product product = productRepository.findById(productId).orElse(null);

            if (product == null) {
                throw new NoSuchElementException("Product not found");
            }
            if (!product.getIsActive()) {
                throw new NotActiveException("Product is already deleted");
            }

            product.setLastUpdateDate(new Date(System.currentTimeMillis()));
            product.setIsActive(false);
            productRepository.save(product);

            return ProductViewDTO.modelToDto(product);
        } catch (Exception e) {
            log.error("Error soft deleting product at Product Service");
            e.printStackTrace();
            throw e;
        }
    }

    public void isValid(Product product, Product tempProduct) throws Exception {
        if (product == null) {
            throw new NoSuchElementException("Product not found");
        }
        if (!product.getIsActive()) {
            throw new NotActiveException("Product is not active");
        }
        if (tempProduct.getPrice() <= 0) {
            throw new IllegalArgumentException("Price cannot be set to 0");
        }
        if (tempProduct.getDiscount() < 0) {
            throw new IllegalArgumentException("Discount must be 0 or greater");
        }
    }

}
