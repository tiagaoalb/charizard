package dev.charizard.messagebroker;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {
	@RabbitListener(queues = "transaction") // Specify the name of the queue to listen to
	public void onTransaction(Object event) {
		System.out.println(event);
	}
}
