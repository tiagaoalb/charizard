package dev.charizard.messagebroker.repositories;

import dev.charizard.messagebroker.models.Installment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentRepository extends JpaRepository<Installment, String> {
}
