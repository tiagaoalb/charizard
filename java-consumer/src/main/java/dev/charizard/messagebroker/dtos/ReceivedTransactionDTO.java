package dev.charizard.messagebroker.dtos;

import java.time.Instant;

public class ReceivedTransactionDTO {
	private String transactionId;
	private Instant transactionDate;
	private String document; //person id
	private String name;
	private Integer age;
	private Double value; //total transaction value
	private Integer installmentQuantity; //numero total de parcelas

	public ReceivedTransactionDTO() {
	}

	public ReceivedTransactionDTO(String transactionId, Instant transactionDate, String document, String name, Integer age, Double value, Integer installmentQuantity) {
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		this.document = document;
		this.name = name;
		this.age = age;
		this.value = value;
		this.installmentQuantity = installmentQuantity;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Instant getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Instant transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Integer getInstallmentQuantity() {
		return installmentQuantity;
	}

	public void setInstallmentQuantity(Integer installmentQuantity) {
		this.installmentQuantity = installmentQuantity;
	}

	@Override
	public String toString() {
		return "ReceivedTransactionDTO{" +
						"transactionId='" + transactionId + '\'' +
						", transactionDate=" + transactionDate +
						", document='" + document + '\'' +
						", name='" + name + '\'' +
						", age=" + age +
						", value=" + value +
						", installmentQuantity=" + installmentQuantity +
						'}';
	}
}
