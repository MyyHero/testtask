CREATE SEQUENCE account_seq START 1;
ALTER TABLE accounts ALTER COLUMN id SET DEFAULT nextval('account_seq');

CREATE SEQUENCE user_seq START 1;
ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('user_seq');

CREATE SEQUENCE email_seq START 1;
ALTER TABLE email_data ALTER COLUMN id SET DEFAULT nextval('email_seq');

CREATE SEQUENCE phone_seq START 1;
ALTER TABLE phone_data ALTER COLUMN id SET DEFAULT nextval('phone_seq');
