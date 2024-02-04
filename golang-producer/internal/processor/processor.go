package processor

import (
	"encoding/csv"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"sync"
)

var wg sync.WaitGroup

type Processor struct {
	InputPath  string
	OutputPath string
}

func (p *Processor) CsvToJson() {
	file, err := os.Open(p.OutputPath)

	if err != nil {
		log.Default().Fatalln("Cannot open the csv output file to encoding to json", err.Error())
	}

	reader := csv.NewReader(file)

	lines, err := reader.ReadAll()

	if err != nil {
		log.Default().Fatalln("Cannot read the csv line, file should be revised", err.Error())
	}

	// fieldCount := len(lines[0])
	// for _, line := range lines {
	// 	if len(line) != fieldCount {
	// 		log.Default().Println("Inconsistent file lines...")
	// 	}
	// }

	json, err := json.Marshal(lines)
	if err != nil {
		log.Default().Fatalln("Cannot convert csv file to json, file should be revised", err)
	}
	fmt.Println(json)
}

func (o *Processor) FlushHeader() {
	log.Default().Println("Read to flush csv...")
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

	headers := []string{"transaction_id", "transaction_date", "document", "status", "name", "age", "value", "installments_number"}
	csvReader := csv.NewReader(data)
	csvReader.Comma = ';'
	copyCsv, err := csvReader.ReadAll()

	if err != nil {
		log.Default().Fatalln("Failed to read the original csv to copy csv", err.Error())
	}

	csvWriter := csv.NewWriter(outputFile)
	defer csvWriter.Flush()

	wg.Add(1)
	go func() {
		defer wg.Done()
		if err := csvWriter.Write(headers); err != nil {
			log.Default().Fatalf("Failed to write header: %s", err.Error())
		}
		for _, lines := range copyCsv {
			if err := csvWriter.Write(lines); err != nil {
				log.Default().Fatalf("Failed to write data in copy csv: %s", err.Error())
			}
			o.CsvToJson()
		}
	}()

	wg.Wait()

}
