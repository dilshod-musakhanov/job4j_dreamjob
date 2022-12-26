CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    city_id INTEGER,
    photo BYTEA,
    created TIMESTAMP
);