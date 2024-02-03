package dev.charizard.messagebroker;


import dev.charizard.messagebroker.dtos.ReceivedTransactionDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {
	@RabbitListener(queues = "transaction") // Specify the name of the queue to listen to
	public void onTransaction(ReceivedTransactionDTO event) {
		System.out.println(event);
	}
}
