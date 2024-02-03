package dev.charizard.messagebroker.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;


@Entity(name = "transaction")
public class Transaction {
	@Id
	private String id; //comes from outside

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "person_id")
	private Person person;

	@Column(name = "transaction_date")
	private Instant transactionDate;

	@Column(name = "amount")
	private Double amount;

	@OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Installment> installments;

	public Transaction() {
	}

	public Transaction(String id, Person person, Instant transactionDate, Double amount, Set<Installment> installments) {
		this.id = id;
		this.person = person;
		this.transactionDate = transactionDate;
		this.amount = amount;
		this.installments = installments;
	}

	public static Transaction create(
					String id,
					Person person,
					Instant transactionDate,
					Double amount
	) {
		var transaction = new Transaction(
						id,
						person,
						transactionDate,
						amount,
						null

		);
		var errors = transaction.validate();
		if (errors.size() > 0) {
			//todo: catch and send to DLQ
		}
		return transaction;
	}

	public List<String> validate() {
		return List.of();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Instant getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Instant transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Set<Installment> getInstallments() {
		return installments;
	}

	public void setInstallments(Set<Installment> installments) {
		this.installments = installments;
	}
}
