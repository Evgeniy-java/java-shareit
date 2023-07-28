--DROP TABLE IF EXISTS users, items, comments, bookings;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(60) NOT NULL,
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(420) NOT NULL,
    is_available BOOLEAN,
    owner_id BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE IF NOT EXISTS booking (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id BIGINT REFERENCES items (id) ON DELETE CASCADE NOT NULL,
    booker_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    status VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    text VARCHAR(300) NOT NULL,
    item_id BIGINT REFERENCES items (id) ON DELETE CASCADE NOT NULL,
    author_id BIGINT REFERENCES users (id) ON DELETE CASCADE  NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE
);