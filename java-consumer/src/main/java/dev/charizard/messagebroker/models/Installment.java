package dev.charizard.messagebroker.models;

import dev.charizard.messagebroker.exceptions.EntityValidationException;
import jakarta.persistence.*;

import java.util.HashSet;
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
		var installment = new Installment(
						UUID.randomUUID().toString(),
						null,
						installmentNumber,
						value
		);
		var errors = installment.validate();
		if (!errors.isEmpty()) {
			throw new EntityValidationException(errors);
		}
		return installment;
	}

	private Set<String> validate() {
		var errors = new HashSet<String>();
		if (installmentNumber == null || installmentNumber <= 0) {
			errors.add("Invalid installment number:" + installmentNumber);
		}
		if (value == null || value <= 0) {
			errors.add("Invalid value:" + value);
		}
		return errors;
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
