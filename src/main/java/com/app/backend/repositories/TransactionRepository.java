package com.app.backend.repositories;

import com.app.backend.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
   Optional<Transaction> findByProductId(UUID id);
}
