# ASAP Hackaton Project - Charizard - Java Consumer

## Run

### From docker image

```bash
docker run -d --network=<your_network> leothenardo/asapcard-java-consumer
```

Note: docker compose is available in the root of the project

## Java consumer Envs:

```${ENV_VARIABLE:defaultValueIfNotSet}```

```properties
${DATABASE_URL:jdbc:postgresql://localhost:5432/db}
${DATABASE_USER:user}
${DATABASE_PASSWORD:pass}
${SHOW_SQL:true}
${RABBITMQ_HOST:localhost}
${RABBITMQ_PORT:5672}
${RABBITMQ_USER:guest}
${RABBITMQ_PASSWORD:guest}
${RABBITMQ_VIRTUAL_HOST:/}
${RABBITMQ_RETRY_MAX_ATTEMPTS:3}
${RABBITMQ_RETRY_INITIAL_INTERVAL:5s}
${RABBITMQ_RETRY_MULTIPLIER:2}
${RABBITMQ_TRANSACTION_QUEUE:transaction}
${RABBITMQ_TRANSACTION_DLX:transaction.dlx}
${RABBITMQ_TRANSACTION_DLQ:transaction.dlq}
${RABBITMQ_CONCILIATION_QUEUE:conciliation}
${RABBITMQ_CONCILIATION_DLX:conciliation.dlx}
${RABBITMQ_CONCILIATION_DLQ:conciliation.dlq}

```

## DTO Payload From Messages Examples

### Transaction

```json
{
  "transactionId": "32d527d4-485a-47ee-a9d5-e95f6b40ede7",
  "transactionDate": "2023-06-15T04:00:43Z",
  "document": "82982060095",
  "name": "Hal Jordan",
  "age": 38,
  "value": 974.80,
  "installmentQuantity": 3
}
```

### Conciliation

```json
{
  "transactionId": "32d527d4-485a-47ee-a9d5-e95f6b40ede7",
  "transactionDate": "2023-06-15T04:00:43Z",
  "document": "82982060095",
  "status": "C"
}
```