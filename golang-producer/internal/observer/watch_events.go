package observer

import (
	"log"

	"github.com/fsnotify/fsnotify"
	"github.com/tiagaoalb/charizard/golang-producer/internal/processor"
)

var (
	p = processor.Processor{InputPath: "./input/input-data.csv", OutputPath: "./output/output-data.csv"}
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
					p.FlushHeader()
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
