package com.app.backend.service;

import com.app.backend.dto.product.ProductViewDTO;
import com.app.backend.model.Product;
import com.app.backend.model.User;
import com.app.backend.repository.ProductRepository;
import com.app.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public ProductViewDTO create(Product product, UUID userId) throws Exception {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new Exception("Error on finding user");
        }
        if (!user.getIsActive()) {
            throw new Exception("User is not active");
        }

        product.setUser(user);
        productRepository.save(product);

        return ProductViewDTO.modelToDto(product);
    }


    public List<ProductViewDTO> getAll() {

        List<ProductViewDTO> productList = new ArrayList<ProductViewDTO>();

        productRepository.findByIsActiveTrue()
                .forEach(product -> productList.add(ProductViewDTO.modelToDto(product)));

        return productList;
    }

    public List<ProductViewDTO> findByUser(UUID userId) throws Exception {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new Exception("User not found");

        List<ProductViewDTO> productList = new ArrayList<ProductViewDTO>();

        productRepository.findByUserAndIsActiveTrue(user)
                .forEach(product -> productList.add(ProductViewDTO.modelToDto(product)));

        return productList;
    }

    public ProductViewDTO update(UUID productId, Product tempProduct) throws Exception {

        Product product = productRepository.findById(productId)
                .orElse(null);

        if (product == null) {
            throw new Exception("Product not found");
        }
        if(!product.getIsActive()){
            throw new Exception("Product is not active");
        }
        if (tempProduct.getPrice() <= 0) {
            throw new Exception("Price cannot be set to 0");
        }
        if (tempProduct.getDiscount() < 0) {
            throw new Exception("Discount must be 0 or greater");
        }

        product.setPrice(tempProduct.getPrice());
        product.setDiscount(tempProduct.getDiscount());
        product.setLastUpdateDate(new Date(System.currentTimeMillis()));
        productRepository.save(product);

        return ProductViewDTO.modelToDto(product);
    }

    public ProductViewDTO delete(UUID productId) throws Exception {
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            throw new Exception("Product not found");
        }
        if (!product.getIsActive()) {
            throw new Exception("Product is already deleted");
        }

        product.setLastUpdateDate(new Date(System.currentTimeMillis()));
        product.setIsActive(false);
        productRepository.save(product);

        return ProductViewDTO.modelToDto(product);
    }

}
