ALTER TABLE transaction
    ADD COLUMN status CHAR(1) DEFAULT ('P') NOT NULL CHECK (status IN ('C', 'N', 'P'));
