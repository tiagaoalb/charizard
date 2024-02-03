package dev.charizard.messagebroker;


import dev.charizard.messagebroker.dtos.ReceivedTransactionDTO;
import dev.charizard.messagebroker.services.TransactionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {
	@Autowired
	TransactionService transactionService;

	@RabbitListener(queues = "transaction") // Specify the name of the queue to listen to
	public void onTransaction(ReceivedTransactionDTO event) {
		try {
			transactionService.processTransaction(event);
		} catch (Exception e) {
			//TODO: Send to dead letter
			throw e;
		}
	}
}
