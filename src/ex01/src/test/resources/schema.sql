DROP TABLE IF EXISTS product CASCADE;

CREATE table IF NOT EXISTS product
(
    id
    INTEGER
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    100
) NOT NULL,
    price INTEGER
    );