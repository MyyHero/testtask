-- USERS ------------------------------------------------------------
create table users (
                       id            bigserial primary key,
                       name          varchar(500) not null,
                       date_of_birth date         not null,
                       password      varchar(500) not null
);

-- ACCOUNTS ---------------------------------------------------------
create table accounts (
                          id       bigserial primary key,
                          user_id  bigint    not null unique,
                          balance  numeric(19,2) not null,
                          initial_deposit numeric(19,2) not null default 0,
                          constraint fk_accounts_user
                              foreign key (user_id) references users(id)
);

-- EMAIL_DATA -------------------------------------------------------
create table email_data (
                            id      bigserial primary key,
                            user_id bigint       not null,
                            email   varchar(200) not null unique,
                            constraint fk_email_user foreign key (user_id) references users(id)
);

-- PHONE_DATA -------------------------------------------------------
create table phone_data (
                            id      bigserial primary key,
                            user_id bigint      not null,
                            phone   varchar(13) not null unique,
                            constraint fk_phone_user foreign key (user_id) references users(id)
);
