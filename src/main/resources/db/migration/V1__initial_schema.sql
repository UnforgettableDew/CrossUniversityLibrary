create table users
(
    id       bigserial primary key,
    email    varchar(64) not null,
    password varchar(255),
    role     varchar(32) not null
);