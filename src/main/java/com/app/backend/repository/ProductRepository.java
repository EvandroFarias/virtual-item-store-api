package com.app.backend.repository;

import com.app.backend.model.Product;
import com.app.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByIsActiveTrue();
    List<Product> findByUserAndIsActiveTrue(User user);
}
