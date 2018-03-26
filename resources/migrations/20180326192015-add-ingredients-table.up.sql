CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE ingredients (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);
