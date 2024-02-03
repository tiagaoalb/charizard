package dev.charizard.messagebroker.services;

import dev.charizard.messagebroker.dtos.ReceivedTransactionDTO;
import dev.charizard.messagebroker.models.Installment;
import dev.charizard.messagebroker.models.Person;
import dev.charizard.messagebroker.models.Transaction;
import dev.charizard.messagebroker.repositories.InstallmentRepository;
import dev.charizard.messagebroker.repositories.PersonRepository;
import dev.charizard.messagebroker.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TransactionService {
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	InstallmentRepository installmentRepository;
	@Autowired
	PersonRepository personRepository;

	@Transactional
	public void processTransaction(ReceivedTransactionDTO comingTransaction) {
		Optional<Person> personFromDb = personRepository.findById(comingTransaction.getDocument());
		if (personFromDb.isEmpty()) {
			var person = Person.create(
							comingTransaction.getDocument(),
							comingTransaction.getName(),
							comingTransaction.getAge()
			);

			var transaction = Transaction.create(
							comingTransaction.getTransactionId(),
							person,
							comingTransaction.getTransactionDate(),
							comingTransaction.getValue()
			);
			var installments = Installment.create(
							1,
							comingTransaction.getValue()
			);
			transaction.setInstallments(Set.of(installments));
			transaction.setPerson(person);
			person.setTransactions(Set.of(transaction));
			installments.setTransaction(transaction);
			transactionRepository.save(transaction);
		}
		//todo: implement user already exists flow

	}
}
