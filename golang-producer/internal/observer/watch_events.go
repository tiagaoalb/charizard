package observer

import (
	"log"
	"strings"

	"github.com/fsnotify/fsnotify"
	"github.com/tiagaoalb/charizard/golang-producer/internal/processor"
)

var (
	i = processor.InputDataProcessor{InputPath: "./input/input-data.csv", OutputPath: "./output/output-data.csv"}
	c = processor.ConciliationDataProcessor{InputPath: "./input/conciliation-data.csv", OutputPath: "./output/output-conciliation-data.csv"}
)

func WatchEvents() {
	log.Default().Println("Initializing watch function, waiting FS events...")
	watcher, err := fsnotify.NewWatcher()
	if err != nil {
		log.Default().Fatalln("Cannot initialize the event watcher", err.Error())
	}
	defer watcher.Close()

	go func() {
		for {
			select {
			case event, ok := <-watcher.Events:
				if !ok {
					return
				}
				if event.Has(fsnotify.Create) || event.Has(fsnotify.Chmod) {
					log.Default().Println("Watching file ->", event.Name)
					log.Default().Println("Executing processor function...")
					if strings.Contains(event.Name, "input-data.csv") {
						log.Default().Println("Processing input data csv...")
						i.FlushNewCsv()
					}
					if strings.Contains(event.Name, "conciliation-data.csv") {
						log.Default().Println("Processing conciliation data csv...")
						c.FlushNewCsv()
					}

				}
			case err, ok := <-watcher.Errors:
				if !ok {
					return
				}
				log.Default().Println("Error during watch files", err.Error())
			}
		}
	}()

	err = watcher.Add("./input")
	if err != nil {
		log.Default().Fatalln("Cannot watch the dir, probably doesn't exist", err.Error())
	}

	<-make(chan struct{})
}
