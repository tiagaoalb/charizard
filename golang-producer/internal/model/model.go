package model

import "time"

type CsvData struct {
	TransactionId      string    `csv:"transaction_id" json:"transactionId"`
	TransactionDate    time.Time `csv:"transaction_date" json:"transactionDate"`
	Document           string    `csv:"document" json:"document"`
	Status             string    `csv:"status" json:"status"`
	Name               string    `csv:"name" json:"name"`
	Age                int       `csv:"age" json:"age"`
	Value              float64   `csv:"value" json:"value"`
	InstallmentsNumber int       `csv:"installments_number" json:"installmentsNumber"`
}
