package dev.charizard.messagebroker.services;

import dev.charizard.messagebroker.dtos.ReceivedConciliationDTO;
import dev.charizard.messagebroker.dtos.ReceivedTransactionDTO;
import dev.charizard.messagebroker.models.Person;
import dev.charizard.messagebroker.models.Transaction;
import dev.charizard.messagebroker.factories.TransactionFactory;
import dev.charizard.messagebroker.repositories.PersonRepository;
import dev.charizard.messagebroker.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	PersonRepository personRepository;

	@Transactional
	public void processTransaction(ReceivedTransactionDTO comingTransaction) {
		transactionRepository.findById(comingTransaction.getTransactionId()).ifPresent(transaction -> {
			return; // já foi processada
		});
		Person person = personRepository.findById(comingTransaction.getDocument()).orElse(null);
		if (person == null) {
			person = Person.create(
							comingTransaction.getDocument(),
							comingTransaction.getName(),
							comingTransaction.getAge()
			);
		}
		Transaction transactionAggregate = TransactionFactory.createTransaction(
						person,
						comingTransaction.getTransactionId(),
						comingTransaction.getTransactionDate(),
						comingTransaction.getValue(),
						comingTransaction.getInstallmentQuantity()
		);
		transactionRepository.save(transactionAggregate);
	}

	@Transactional
	public void processConciliation(ReceivedConciliationDTO comingConciliation) {
		Transaction transaction = transactionRepository.findById(comingConciliation.getTransactionId()).orElse(null);
		if (transaction == null) {
			throw new RuntimeException("Transaction not found");
		}
		transaction.setStatus(comingConciliation.toTransactionStatus());
		transactionRepository.save(transaction);
	}
}
