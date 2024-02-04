package dev.charizard.messagebroker;


import dev.charizard.messagebroker.dtos.ReceivedTransactionDTO;
import dev.charizard.messagebroker.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {
	private static Logger LOG = LoggerFactory.getLogger(TransactionConsumer.class);
	@Autowired
	TransactionService transactionService;

	@RabbitListener(queues = "transaction") // Specify the name of the queue to listen to
	public void onTransaction(ReceivedTransactionDTO event) {
		try {
			transactionService.processTransaction(event);
			LOG.info("Processed transaction " + event.getTransactionId());

		} catch (Exception e) {
			LOG.error("Error processing transaction " + event.getTransactionId(), e);
			throw e; // This will make the message be requeued 3 times and then sent to the dead letter
		}
	}
}
