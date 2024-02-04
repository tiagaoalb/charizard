package dev.charizard.messagebroker.factories;

import dev.charizard.messagebroker.models.Installment;
import dev.charizard.messagebroker.models.Person;
import dev.charizard.messagebroker.models.Transaction;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class TransactionFactory { // create transaction aggregate
	public static Transaction createTransaction(Person person, String id, Instant transactionDate, Double transactionValue, Integer quantityInstallments) {
		var transaction = Transaction.create(
						id,
						person,
						transactionDate,
						transactionValue
		);

		Set<Installment> newInstallments = new HashSet<>();
		for (int i = 1; i <= quantityInstallments; i++) { // um installment para cada parcela
			var installment = Installment.create(
							i,
							transactionValue / quantityInstallments
			);
			installment.setTransaction(transaction);
			newInstallments.add(installment);
		}

		transaction.setInstallments(newInstallments);
		transaction.setPerson(person);

		return transaction;
	}
}
