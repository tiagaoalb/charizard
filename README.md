# Asapcard Hackaton Project - Team Charizard

## Integrantes
- https://github.com/Leoff00
- https://github.com/tiagaoalb
- https://github.com/leo-the-nardo

O desafio consiste em desenvolver dois microsserviços.
É exigido que o candidato codifique um programa capaz de ler um arquivo CSV e publicar, para
cada linha do arquivo, uma mensagem em formato JSON de acordo com um critério de
parse.
Outro programa deve ser construído para consumir as mensagens publicadas e persistir
os dados em um modelo relacional.

Tecnologias utilizadas: 
- Golang
- Java
- RabbitMQ
- PostgreSQL
- Docker

## Run
```sh
git clone https://github.com/tiagaoalb/charizard.git

cd charizard

docker compose up -d
```

Pronto ! Aguardando arquivos nas pastas input & conciliation.

## RabbitMQ Panel

```sh
http://localhost:15672/
```

- user: guest
- password: guest

## Postgres

```sh
postgresql://user:pass@localhost:5432/db
```
