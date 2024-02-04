package observer

import (
	"log"
	"strings"

	"github.com/fsnotify/fsnotify"
	"github.com/tiagaoalb/charizard/golang-producer/internal/processor"
)

var (
	i = processor.InputDataProcessor{InputPath: "./input/input-data.csv", OutputPath: "./output/output-data.csv"}
	c = processor.ConciliationDataProcessor{InputPath: "./conciliation/conciliation-data.csv", OutputPath: "./output/output-conciliation-data.csv"}
)

func WatchEvents() {
	watcher, err := fsnotify.NewWatcher()
	if err != nil {
		log.Fatal(err)
	}
	defer watcher.Close()

	diretorios := []string{
		"./input",
		"./conciliation",
	}

	for _, dir := range diretorios {
		err := watcher.Add(dir)
		if err != nil {
			log.Fatal(err)
		}
		log.Printf("Observando o diretório: %s\n", dir)
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
					c.FlushNewCsv()
				}
				if strings.Contains(event.Name, "input-data.csv") {
					log.Default().Println("Watching file ->", event.Name)
					log.Default().Println("Executing processor function...")
					log.Default().Println("Processing input data csv...")
					i.FlushNewCsv()
				}
			}
		case err, ok := <-watcher.Errors:
			if !ok {
				return
			}
			log.Println("Erro do observador:", err)
		}
	}
}
