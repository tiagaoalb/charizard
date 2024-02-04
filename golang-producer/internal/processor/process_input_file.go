package processor

import (
	"encoding/csv"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"strconv"
	"sync"
	"time"

	"github.com/tiagaoalb/charizard/golang-producer/internal/model"
)

var wg sync.WaitGroup

type InputDataProcessor struct {
	InputPath string
}

func (o *InputDataProcessor) FlushInput() {
	log.Default().Println("Read to flush csv...")
	workers := 2
	data, err := os.Open(o.InputPath)

	if err != nil {
		log.Default().Fatalln("Failed in open file, file do not exist in folder", err.Error())
	}

	defer data.Close()

	if err != nil {
		log.Default().Fatalln("Failed in create file", err.Error())
	}

	csvReader := csv.NewReader(data)
	csvReader.Comma = ';'
	reader, err := csvReader.ReadAll()

	if err != nil {
		log.Default().Fatalln("Failed to read the original csv to copy csv", err.Error())
	}

	linesChan := make(chan []string, 500)
	wg.Add(workers)

	var inputArr []model.Input

	go func() {
		defer wg.Done()
		for lines := range linesChan {
			date, _ := time.Parse(time.RFC3339, lines[1])
			age, _ := strconv.Atoi(lines[4])
			value, _ := strconv.ParseFloat(lines[5], 64)
			installmentNumber, _ := strconv.Atoi(lines[6])
			input := model.Input{
				TransactionId:      lines[0],
				TransactionDate:    date,
				Document:           lines[2],
				Name:               lines[3],
				Age:                age,
				Value:              value,
				InstallmentsNumber: installmentNumber,
			}
			inputArr = append(inputArr, input)
		}
		j, err := json.MarshalIndent(inputArr, "", " ")
		if err != nil {
			log.Default().Fatalf("Failed to write data in copy csv: %s", err.Error())
		}
		// queue.PublishInput(string(j))
		fmt.Println(string(j))
	}()

	for i := 0; i < workers; i++ {
		go func() {
			defer func() {
				wg.Done()
			}()
			for _, lines := range reader {
				linesChan <- lines
			}
		}()
	}

	wg.Wait()
	close(linesChan)

}
