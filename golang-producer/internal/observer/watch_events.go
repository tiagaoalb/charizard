package observer

import (
	"log"
	"strings"

	amqp "github.com/rabbitmq/amqp091-go"

	"github.com/fsnotify/fsnotify"
	"github.com/tiagaoalb/charizard/golang-producer/internal/processor"
)

var (
	i = processor.InputDataProcessor{InputPath: "./input/input-data.csv"}
	c = processor.ConciliationDataProcessor{InputPath: "./conciliation/conciliation-data.csv"}
)

func WatchEvents(conn *amqp.Connection) {
	watcher, err := fsnotify.NewWatcher()
	if err != nil {
		log.Fatal(err)
	}
	defer watcher.Close()

	dirs := []string{
		"./input",
		"./conciliation",
	}

	for _, dir := range dirs {
		err := watcher.Add(dir)
		if err != nil {
			log.Fatal(err)
		}
		log.Default().Printf("Watching dir ->: %s\n", dir)
	}

	for {
		select {
		case event, ok := <-watcher.Events:
			if !ok {
				return
			}
			if event.Has(fsnotify.Create) {
				if strings.Contains(event.Name, "conciliation-data.csv") {
					log.Default().Println("Watching file ->", event.Name)
					log.Default().Println("Executing processor function...")
					log.Default().Println("Processing conciliation data csv...")
					c.FlushConciliation(conn)
				}
				if strings.Contains(event.Name, "input-data.csv") {
					log.Default().Println("Watching file ->", event.Name)
					log.Default().Println("Executing processor function...")
					log.Default().Println("Processing input data csv...")
					i.FlushInput(conn)
				}
			}
		case err, ok := <-watcher.Errors:
			if !ok {
				return
			}
			log.Default().Println("Erro during observe files:", err)
		}
	}
}
