-- USERS ----------------------------------------------------------------------
INSERT INTO users (name, date_of_birth, password)
VALUES  ('Alice', '1990-01-01', '$2a$12$PA4uzULHXXLW4uxmOPFqpefOq.iJkAQsnj4UabtlAhBl1GucqSb.y'), -- bcrypt(pwd1)
        ( 'Bob',   '1995-02-02', '$2a$12$/TaOVtT516EoTboWWevfkuTkWw/HNiKRmYv3vqBzOk5hXFi02aoxq'); -- bcrypt(pwd2)

-- EMAIL_DATA -----------------------------------------------------------------
INSERT INTO email_data ( user_id, email)
VALUES  ( 1, 'alice@ex.com'),
        ( 2, 'bob@ex.com');

-- PHONE_DATA -----------------------------------------------------------------
INSERT INTO phone_data ( user_id, phone)
VALUES  ( 1, '75550000006'),
        ( 2, '75550000086');

-- ACCOUNTS -------------------------------------------------------------------
INSERT INTO accounts ( user_id, balance, initial_deposit)
VALUES  ( 1, 1000.00, 1000),
        (2,   50.00, 50);
