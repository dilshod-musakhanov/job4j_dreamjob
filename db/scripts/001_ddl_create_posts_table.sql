CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    visible BOOLEAN,
    city_id INTEGER,
    created TIMESTAMP
);