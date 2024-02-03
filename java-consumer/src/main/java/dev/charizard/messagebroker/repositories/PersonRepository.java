package dev.charizard.messagebroker.repositories;

import dev.charizard.messagebroker.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {
}
