package dev.charizard.messagebroker.configs;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableRabbit
public class RabbitMQConfig {

	@Value("${rabbitmq.transaction.queue}")
	private String TRANSACTION_QUEUE;
	@Value("${rabbitmq.conciliation.queue}")
	private String CONCILIATION_QUEUE;
	@Value("${rabbitmq.transaction.dlx}")
	private String TRANSACTION_DLX;
	@Value("${rabbitmq.conciliation.dlx}")
	private String CONCILIATION_DLX;
	@Value("${rabbitmq.transaction.dlq}")
	private String TRANSACTION_DLQ;
	@Value("${rabbitmq.conciliation.dlq}")
	private String CONCILIATION_DLQ;


	@Bean
	public Queue transactionQueue() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-dead-letter-exchange", TRANSACTION_DLX); // exchange vai fazer o roteamento para a fila de dead letter
		//args.put("x-dead-letter-routing-key", "transaction.dlq"); //se preferir enviar direto para a fila
		return new Queue(TRANSACTION_QUEUE, true, false, false, args);
	}

	@Bean
	public Queue conciliationQueue() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-dead-letter-exchange", CONCILIATION_DLX);
		return new Queue(CONCILIATION_QUEUE, true, false, false, args);
	}

	@Bean
	public FanoutExchange fanoutExchangeTransactionDLX() { //Dead letter exchange
		return new FanoutExchange(TRANSACTION_DLX);
	}

	@Bean
	public FanoutExchange fanoutExchangeConciliationDLX() { //Dead letter exchange
		return new FanoutExchange(CONCILIATION_DLQ);
	}

	@Bean
	public Queue transactionQueueDLQ() { //Dead letter queue
		return new Queue(TRANSACTION_DLQ);
	}

	@Bean
	public Queue conciliationQueueDLQ() { //Dead letter queue
		return new Queue(CONCILIATION_DLQ);
	}

	@Bean
	public Binding bindingTransactionDLQ() {
		Queue queue = transactionQueueDLQ();
		FanoutExchange exchange = fanoutExchangeTransactionDLX();
		return BindingBuilder.bind(queue).to(exchange);
	}

	@Bean
	public Binding bindingConciliationDLQ() {
		Queue queue = conciliationQueueDLQ();
		FanoutExchange exchange = fanoutExchangeConciliationDLX();
		return BindingBuilder.bind(queue).to(exchange);
	}

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	@Bean
	public ApplicationListener<ApplicationReadyEvent> onApplicationReadyEventListener(RabbitAdmin rabbitAdmin) {
		return event -> rabbitAdmin.initialize();
	}

	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}


}