package dev.charizard.messagebroker.repositories;

import dev.charizard.messagebroker.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
