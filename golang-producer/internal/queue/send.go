package queue

import (
	"context"
	"log"
	"time"

	amqp "github.com/rabbitmq/amqp091-go"
)

func failOnError(err error, msg string) {
	if err != nil {
		log.Default().Panicf("%s: %s", msg, err)
	}
}

func connect() *amqp.Connection {
	conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
	failOnError(err, "Failed to connect to RabbitMQ")
	return conn
}

func PublishConciliation(data string) {
	conn := connect()
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	q, err := ch.QueueDeclare("pruducer_queue", true, false, false, false, nil)
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

	q, err := ch.QueueDeclare("pruducer_queue", true, false, false, false, nil)
	failOnError(err, "Failed to declare a queue")

	defer cancel()

	err = ch.PublishWithContext(ctx, "", q.Name, false, false,
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(data),
		})
	failOnError(err, "Failed to publish a message")
}
