package main

import (
	"github.com/tiagaoalb/charizard/golang-producer/internal/observer"
	"log"
)

func main() {
	log.Default().Println("[GOLANG PRODUCER] - START WATCHING EVENTS")
	observer.WatchEvents()

	// stsignal := make(chan os.Signal, 1)
	// signal.Notify(stsignal, syscall.SIGINT, syscall.SIGTERM, os.Interrupt)
	// <-stsignal
}
