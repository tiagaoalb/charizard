package queue

import (
	"context"
	"fmt"
	amqp "github.com/rabbitmq/amqp091-go"
	"github.com/tiagaoalb/charizard/golang-producer/pkg/env"
	"log"
	"time"
)

var (
	host                  = env.Getenv("rabbitmq_host")
	port                  = env.Getenv("rabbitmq_port")
	user                  = env.Getenv("rabbitmq_user")
	pass                  = env.Getenv("rabbitmq_pass")
	vhost                 = env.Getenv("rabbitmq_vhost")
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

func connect() *amqp.Connection {
	RabbitURL := fmt.Sprintf("amqp://%s:%s@%s:%s/%s", user, pass, host, port, vhost)
	conn, err := amqp.Dial(RabbitURL)
	failOnError(err, "Failed to connect to RabbitMQ")
	return conn
}

func PublishConciliation(data string) {
	if data == "" {
		return
	}
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

	log.Default().Println("Pub conciliation data: ", data)
	err = ch.PublishWithContext(ctx, "", q.Name, false, false,
		amqp.Publishing{
			ContentType: "application/json",
			Body:        []byte(data),
		})
	failOnError(err, "Failed to publish a message")
}

func PublishInput(data string) {
	if data == "" {
		return
	}
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

	log.Default().Println("Pub transaction data: ", data)
	err = ch.PublishWithContext(ctx, "", q.Name, false, false,
		amqp.Publishing{
			ContentType: "application/json",
			Body:        []byte(data),
		})
	failOnError(err, "Failed to publish a message")
}
