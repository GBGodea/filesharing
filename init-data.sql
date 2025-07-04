CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS roles (
    id   UUID PRIMARY KEY,
    role VARCHAR(50) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS users (
    id       UUID PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id  UUID NOT NULL REFERENCES roles(id)
    );

CREATE TABLE IF NOT EXISTS files (
    id               UUID PRIMARY KEY,
    original_name    VARCHAR(255) NOT NULL,
    stored_name      VARCHAR(255) NOT NULL UNIQUE,
    content_type     VARCHAR(255),
    size             BIGINT,
    upload_date      TIMESTAMP NOT NULL,
    expiration_date  TIMESTAMP NOT NULL,
    download_count   BIGINT NOT NULL DEFAULT 0
    );

INSERT INTO roles(id, role)
SELECT gen_random_uuid(),
       'user' WHERE NOT EXISTS(
    SELECT 1 FROM roles WHERE role = 'user'
);

INSERT INTO roles(id, role)
SELECT gen_random_uuid(),
       'admin' WHERE NOT EXISTS(
    SELECT 1 FROM roles WHERE role = 'admin'
);

INSERT INTO users(id, email, password, role_id)
SELECT gen_random_uuid(),
       'admin@admin.ru',
       'admin',
       r.id
FROM roles r
WHERE r.role = 'admin'
  AND NOT EXISTS(
        SELECT 1 FROM users WHERE email = 'admin@admin.ru'
    );