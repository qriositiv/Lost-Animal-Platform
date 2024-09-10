CREATE TABLE IF NOT EXISTS "User" (
    id VARCHAR(40) NOT NULL,
    first_name VARCHAR(63) NOT NULL,
    last_name VARCHAR(63) NOT NULL,
    username VARCHAR(25) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    user_photo BYTEA,
    email VARCHAR(63) NOT NULL,
    telephone VARCHAR(63),
    role VARCHAR(15) NOT NULL,
    updated_at TIMESTAMP DEFAULT now(),
    created_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (id)
);


--- Also we need triggers so we know hen user updates or created account :
CREATE
OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= now();
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

--- command to add triggers to table
CREATE TRIGGER update_status_modtime
    BEFORE UPDATE
    ON "User"
    FOR EACH ROW
    EXECUTE FUNCTION update_modified_column();