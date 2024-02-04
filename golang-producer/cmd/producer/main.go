package main

import (
	"fmt"
	"log"

	amqp "github.com/rabbitmq/amqp091-go"

	"github.com/tiagaoalb/charizard/golang-producer/internal/observer"
	"github.com/tiagaoalb/charizard/golang-producer/pkg/env"
)

var (
	host  = env.Getenv("rabbitmq_host")
	port  = env.Getenv("rabbitmq_port")
	user  = env.Getenv("rabbitmq_user")
	pass  = env.Getenv("rabbitmq_pass")
	vhost = env.Getenv("rabbitmq_vhost")
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
	log.Default().Println("Connected with RABBITMQ")
	return conn
}

func main() {
	log.Default().Println("[GOLANG PRODUCER] - START WATCHING EVENTS")
	conn := connect()
	observer.WatchEvents(conn)
}
