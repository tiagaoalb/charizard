package processor

import (
	"encoding/csv"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"time"

	amqp "github.com/rabbitmq/amqp091-go"

	"github.com/tiagaoalb/charizard/golang-producer/internal/model"
	"github.com/tiagaoalb/charizard/golang-producer/internal/queue"
)

type ConciliationDataProcessor struct {
	InputPath string
}

func (o *ConciliationDataProcessor) FlushConciliation(conn *amqp.Connection) {
	log.Default().Println("Read to flush csv...")
	var toJson []byte
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

	var conArr []model.Conciliation

	for _, lines := range reader {
		date, _ := time.Parse(time.RFC3339, lines[1])
		con := model.Conciliation{
			TransactionId:   lines[0],
			TransactionDate: date,
			Document:        lines[2],
			Status:          lines[3],
		}
		conArr = append(conArr, con)

		toJson, err = json.MarshalIndent(conArr, "", " ")
		if err != nil {
			log.Default().Fatalf("Failed to write data in copy csv: %s", err.Error())
		}
		fmt.Println(string(toJson))
		queue.PublishConciliation(conn, string(toJson))
	}
}
