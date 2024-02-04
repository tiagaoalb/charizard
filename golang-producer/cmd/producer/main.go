package main

import (
	"log"

	"github.com/tiagaoalb/charizard/golang-producer/internal/observer"
)

func main() {
	log.Default().Println("[GOLANG PRODUCER] - START WATCHING EVENTS")
	observer.WatchEvents()
}
