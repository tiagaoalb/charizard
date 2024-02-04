package queue

import (
	"context"
	"log"
	"time"

	amqp "github.com/rabbitmq/amqp091-go"
	"github.com/tiagaoalb/charizard/golang-producer/pkg/env"
)

var (
	ConciliationQueueName = env.Getenv("rabbitmq_conciliation_queue")
	ConciliationDLX       = env.Getenv("rabbitmq_conciliation_dlx")
	TransactionQueueName  = env.Getenv("rabbitmq_transaction_queue")
	TransactionDLX        = env.Getenv("rabbitmq_transaction_dlx")
)

func failOnError(err error, msg string) {
	if err != nil {
		log.Default().Panicf("%s: %s", msg, err)
	}
}
func PublishConciliation(conn *amqp.Connection, data string) {
	defer conn.Close()

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	ch, err := conn.Channel()
	if err != nil {
		log.Fatalf("Failed to open a channel: %v", err)
	}
	defer ch.Close()

	args := amqp.Table{
		"x-dead-letter-exchange": ConciliationDLX,
	}

	q, err := ch.QueueDeclare(ConciliationQueueName, true, false, false, false, args)
	if err != nil {
		log.Fatalf("Failed to declare a queue: %v", err)
	}

	err = ch.PublishWithContext(ctx, "", q.Name, false, false,
		amqp.Publishing{
			ContentType: "application/json",
			Body:        []byte(data),
		})
	if err != nil {
		log.Fatalf("Failed to publish a message: %v", err)
	}
}

func PublishInput(conn *amqp.Connection, data string) {
	defer conn.Close()
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

	log.Default().Println("Pub transaction data: ", data)
	err = ch.PublishWithContext(ctx, "", q.Name, false, false,
		amqp.Publishing{
			ContentType: "application/json",
			Body:        []byte(data),
		})
	failOnError(err, "Failed to publish a message")
}
