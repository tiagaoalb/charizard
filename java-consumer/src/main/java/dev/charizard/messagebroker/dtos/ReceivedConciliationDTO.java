package dev.charizard.messagebroker.dtos;

import dev.charizard.messagebroker.models.TransactionStatus;

import java.time.Instant;

public class ReceivedConciliationDTO {
	private String transactionId;
	private Instant transactionDate;
	private String document; //person id
	private ConciliationStatusDTO status;

	public ReceivedConciliationDTO() {
	}

	public ReceivedConciliationDTO(String transactionId, Instant transactionDate, String document, ConciliationStatusDTO status) {
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		this.document = document;
		this.status = status;
	}

	public TransactionStatus toTransactionStatus() {
		return switch (status) {
			case C -> TransactionStatus.C;
			case N -> TransactionStatus.N;
			default -> null; //Entity will validate this and throw.
		};
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

	public ConciliationStatusDTO getStatus() {
		return status;
	}

	public void setStatus(ConciliationStatusDTO status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ReceivedConciliationDTO{" +
						"transactionId='" + transactionId + '\'' +
						", transactionDate=" + transactionDate +
						", document='" + document + '\'' +
						", status=" + status +
						'}';
	}
}
