# ASAP Hackaton Project - Team Charizard

## Integrantes

https://github.com/Leoff00
https://github.com/tiagaoalb
https://github.com/leo-the-nardo

O desafio consiste em desenvolver dois microsserviços.
É exigido que o candidato codifique um programa capaz de ler um arquivo CSV e publicar, para
cada linha do arquivo, uma mensagem em formato JSON de acordo com um critério de
parse.
Um outro programa deve ser construído para consumir as mensagens publicadas e persistir
os dados em um modelo relacional

Tecnologias utilizadas:

- Golang
- Java
- RabbitMQ

## Run

`docker compose up -d`

Rabbitmq panel

`http://localhost:15672/`

Postgres

`postgresql://user:pass@localhost:5432/db`
