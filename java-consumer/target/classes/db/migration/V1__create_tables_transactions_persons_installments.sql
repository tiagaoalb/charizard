CREATE TABLE person
(
    id   VARCHAR(16) PRIMARY KEY,
    name VARCHAR(255),
    age  INTEGER
);

CREATE TABLE transaction
(
    id               VARCHAR(36) PRIMARY KEY,
    person_id        VARCHAR(16),
    transaction_date TIMESTAMP,
    amount           DOUBLE PRECISION,
    FOREIGN KEY (person_id) REFERENCES person (id)
);

CREATE TABLE installment
(
    id                 VARCHAR(36) PRIMARY KEY,
    transaction_id     VARCHAR(36),
    installment_number INTEGER,
    value              DOUBLE PRECISION,
    FOREIGN KEY (transaction_id) REFERENCES transaction (id)
);