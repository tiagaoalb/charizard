package queue

import (
	"context"
	amqp "github.com/rabbitmq/amqp091-go"
	"log"
	"time"
)

const (
	ConciliationQueueName = "conciliation"
	ConciliationDLX       = "conciliation.dlx"
	TransactionQueueName  = "transaction"
	TransactionDLX        = "transaction.dlx"
	RabbitURL             = "amqp://guest:guest@localhost:5672/"
)

func failOnError(err error, msg string) {
	if err != nil {
		log.Default().Panicf("%s: %s", msg, err)
	}
}

func connect() *amqp.Connection {
	conn, err := amqp.Dial(RabbitURL)
	failOnError(err, "Failed to connect to RabbitMQ")
	return conn
}

func PublishConciliation(data string) {
	conn := connect()
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	args := amqp.Table{
		"x-dead-letter-exchange": ConciliationDLX,
	}
	q, err := ch.QueueDeclare(ConciliationQueueName, true, false, false, false, args)
	failOnError(err, "Failed to declare a queue")

	defer cancel()

	err = ch.PublishWithContext(ctx, "", q.Name, false, false,
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(data),
		})
	failOnError(err, "Failed to publish a message")
}

func PublishInput(data string) {
	conn := connect()
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()
	args := amqp.Table{
		"x-dead-letter-exchange": TransactionDLX,
	}
	q, err := ch.QueueDeclare(TransactionQueueName, true, false, false, false, args)
	failOnError(err, "Failed to declare a queue")

	defer cancel()

	err = ch.PublishWithContext(ctx, "", q.Name, false, false,
		amqp.Publishing{
			ContentType: "application/json",
			Body:        []byte(data),
		})
	failOnError(err, "Failed to publish a message")
}
