package dev.charizard.messagebroker.models;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity(name = "installment")
public class Installment {
	@Id
	@Column(name = "id", length = 36)
	private String id;

	@ManyToOne
	@JoinColumn(name = "transaction_id")
	private Transaction transaction;

	@Column(name = "installment_number")
	private Integer installmentNumber; // qual a parcela

	@Column(name = "value")
	private Double value;

	public Installment() {
	}

	public Installment(String id, Transaction transaction, Integer installmentNumber, Double value) {
		this.id = id;
		this.transaction = transaction;
		this.installmentNumber = installmentNumber;
		this.value = value;
	}

	public static Installment create(Integer installmentNumber, Double value) {
		//TODO: domain logic
		var installment = new Installment(
						UUID.randomUUID().toString(),
						null,
						installmentNumber,
						value
		);
		var errors = installment.validate();
		if (!errors.isEmpty()) {
			//TODO: explode to DLQ
			throw new RuntimeException("Invalid Installment");
		}
		return installment;
	}

	private Set<String> validate() {
		return Set.of();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Integer getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(Integer installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
