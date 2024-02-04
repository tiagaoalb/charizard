package dev.charizard.messagebroker.listeners;


import dev.charizard.messagebroker.dtos.ReceivedConciliationDTO;
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


	@RabbitListener(queues = "${rabbitmq.conciliation.queue}") // Specify the name of the queue to listen to
	public void onConciliation(ReceivedConciliationDTO event) {
		try {
			transactionService.processConciliation(event);
			LOG.info("Conciliation processed for transaction " + event.getTransactionId());
		} catch (Exception e) {
			LOG.error("Error processing conciliation for transaction " + event.getTransactionId(), e);
			throw e; // This will make the message be requeued 3 times and then sent to the dead letter 
		}
	}
}
