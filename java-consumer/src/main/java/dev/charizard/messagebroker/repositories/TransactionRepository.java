package dev.charizard.messagebroker.repositories;

import dev.charizard.messagebroker.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
