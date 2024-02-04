package processor

import (
	"encoding/csv"
	"encoding/json"
	"fmt"
	"github.com/tiagaoalb/charizard/golang-producer/internal/queue"
	"log"
	"os"
	"sync"
)

var wg sync.WaitGroup

type InputDataProcessor struct {
	InputPath  string
	OutputPath string
}

func (p *InputDataProcessor) CsvToJson() {
	var toJson []byte
	file, err := os.Open(p.OutputPath)

	if err != nil {
		log.Default().Fatalln("Cannot open the csv output file to encoding to json", err.Error())
	}

	reader := csv.NewReader(file)
	reader.Comma = ';'

	lines, err := reader.ReadAll()

	if err != nil {
		log.Default().Fatalln("Cannot read the csv line, file should be revised", err.Error())
	}

	for _, each := range lines {
		go func(each []string) {
			toJson, err = json.Marshal(each)
			if err != nil {
				log.Default().Fatalln("Cannot convert csv file to json, file should be revised", err)
			}
		}(each)
	}

	fmt.Println(string(toJson))
<<<<<<< Updated upstream
	queue.PublishInput(string(toJson))
=======
	// queue.PublishConciliation(string(toJson))
>>>>>>> Stashed changes
}

func (o *InputDataProcessor) FlushNewCsv() {
	log.Default().Println("Read to flush csv...")
	workers := 5
	data, err := os.Open(o.InputPath)

	if err != nil {
		log.Default().Fatalln("Failed in open file, file do not exist in folder", err.Error())
	}

	defer data.Close()

	outputFile, err := os.Create(o.OutputPath)

	if err != nil {
		log.Default().Fatalln("Failed in create file", err.Error())
	}

	defer outputFile.Close()

	headers := []string{"transaction_id", "transaction_date", "document", "name", "age", "value", "installments_number"}
	csvReader := csv.NewReader(data)
	csvReader.Comma = ';'
	copyCsv, err := csvReader.ReadAll()

	if err != nil {
		log.Default().Fatalln("Failed to read the original csv to copy csv", err.Error())
	}

	csvWriter := csv.NewWriter(outputFile)
	defer csvWriter.Flush()

	if err := csvWriter.Write(headers); err != nil {
		log.Default().Fatalf("Failed to write header: %s", err.Error())
	}

	linesChan := make(chan []string, 500)
	wg.Add(workers)

	go func() {
		defer wg.Done()
		for lines := range linesChan {
			if err := csvWriter.Write(lines); err != nil {
				log.Default().Fatalf("Failed to write data in copy csv: %s", err.Error())
			}
			o.CsvToJson()
		}
	}()

	for i := 0; i < workers; i++ {
		go func() {
			defer func() {
				wg.Done()
			}()
			for _, lines := range copyCsv {
				linesChan <- lines
			}
		}()
	}

	wg.Wait()
	close(linesChan)

}
