GOBUILD=go build
MAIN_DIR=cmd/producer
BINARY_NAME=producer
TARGET_DIR=bin

install:
	go get ./... && go install ./...

build:
	$(GOBUILD) -o $(TARGET_DIR)/$(BINARY_NAME) $(MAIN_DIR)

local:
	go run $(MAIN_DIR)/main.go

tests:
	go test ./...

release:

clean:
	go clean
	rm -f $(TARGET_DIR)/$(BINARY_NAME)


.PHONY: install build local tests release clean