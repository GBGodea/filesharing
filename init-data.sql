CREATE EXTENSION IF NOT EXISTS pgcrypto;

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