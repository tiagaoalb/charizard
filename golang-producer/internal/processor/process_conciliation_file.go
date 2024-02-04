package processor

import (
	"encoding/csv"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"time"

	"github.com/tiagaoalb/charizard/golang-producer/internal/model"
)

type ConciliationDataProcessor struct {
	InputPath string
}

func (o *ConciliationDataProcessor) FlushConciliation() {
	log.Default().Println("Read to flush csv...")
	workers := 2
	data, err := os.Open(o.InputPath)

	if err != nil {
		log.Default().Fatalln("Failed in open file, file do not exist in folder", err.Error())
	}

	defer data.Close()

	csvReader := csv.NewReader(data)
	csvReader.Comma = ';'
	reader, err := csvReader.ReadAll()

	if err != nil {
		log.Default().Fatalln("Failed to read the original csv to copy csv", err.Error())
	}

	linesChan := make(chan []string, 500)
	wg.Add(workers)

	var conArr []model.Conciliation

	go func() {
		defer wg.Done()
		for lines := range linesChan {
			date, _ := time.Parse(time.RFC3339, lines[1])
			con := model.Conciliation{
				TransactionId:   lines[0],
				TransactionDate: date,
				Document:        lines[2],
				Status:          lines[3],
			}
			conArr = append(conArr, con)
		}
		j, err := json.MarshalIndent(conArr, "", " ")
		if err != nil {
			log.Default().Fatalf("Failed to write data in copy csv: %s", err.Error())
		}
		// queue.PublishConciliation(string(j))
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
