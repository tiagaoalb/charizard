package dev.charizard.messagebroker;


import dev.charizard.messagebroker.dtos.ReceivedConciliationDTO;
import dev.charizard.messagebroker.dtos.ReceivedTransactionDTO;
import dev.charizard.messagebroker.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConciliationConsumer {
	private static Logger LOG = LoggerFactory.getLogger(ConciliationConsumer.class);
	@Autowired
	TransactionService transactionService;

	@RabbitListener(queues = "conciliation") // Specify the name of the queue to listen to
	public void onConciliation(ReceivedConciliationDTO event) {
		try {
			transactionService.processConciliation(event);
		} catch (Exception e) {
			//TODO: Send to dead letter
			LOG.error("Error processing transaction", e);
		}
	}
}
