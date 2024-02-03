package dev.charizard.messagebroker.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Set;


@Entity(name = "transaction")
public class Transaction{
	@Id
	private String id; //comes from outside

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	private Person person;

	@Column(name = "transaction_date")
	private Instant transactionDate;

	@Column(name = "amount")
	private String amount;

	@OneToMany(mappedBy = "transaction")
	private Set<Installment> installments;

	public Transaction() {
	}

	public Transaction(String id, Person person, Instant transactionDate, String amount, Set<Installment> installments) {
		this.id = id;
		this.person = person;
		this.transactionDate = transactionDate;
		this.amount = amount;
		this.installments = installments;
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Set<Installment> getInstallments() {
		return installments;
	}

	public void setInstallments(Set<Installment> installments) {
		this.installments = installments;
	}
}
