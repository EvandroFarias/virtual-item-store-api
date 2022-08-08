package com.app.backend.repositories;

import com.app.backend.models.Product;
import com.app.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByIsActiveTrue();
    List<Product> findByUserAndIsActiveTrue(User user);
}
