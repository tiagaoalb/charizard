package dev.charizard.messagebroker.repositories;

import dev.charizard.messagebroker.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PersonRepository extends JpaRepository<Person, String> {
}
