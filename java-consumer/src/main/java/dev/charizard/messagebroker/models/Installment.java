package dev.charizard.messagebroker.models;

import jakarta.persistence.*;

@Entity(name = "installment")
public class Installment {
	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "transaction")
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

	public static Installment create(){
		//TODO: domain logic
		return new Installment();
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
