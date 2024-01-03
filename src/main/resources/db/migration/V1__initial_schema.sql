create table user_role
(
    id        bigserial primary key,
    role_name varchar(64) not null
);

create table user_credentials
(
    id       bigserial primary key,
    email    varchar(64) unique not null,
    password varchar(255)       not null,
    role_id  bigint,
    foreign key (role_id) references user_role (id)
        on delete cascade
        on update cascade
);

create table university
(
    id     bigserial primary key,
    title  varchar(255) not null,
    domain varchar(255) not null unique
);

create table university_user
(
    id                  bigserial primary key,
    user_name          varchar(64) not null,
    space               float,
    university_id       bigint,
    user_credentials_id bigint unique,
    foreign key (university_id) references university (id)
        on delete cascade
        on update cascade,
    foreign key (user_credentials_id) references user_credentials (id)
        on delete cascade
        on update cascade
);

create table library
(
    id             bigserial primary key,
    title          varchar(255) not null,
    topic          varchar(128),
    library_access boolean,
    university_id       bigint,
    foreign key (university_id) references university (id)
        on delete cascade
        on update cascade
);

create table documents
(
    id          bigserial primary key,
    title       varchar(255) not null,
    topic       varchar(128),
    description varchar(1024),
    file_path   varchar(255) not null,
    owner_id    bigint,
    foreign key (owner_id) references university_user (id)
        on delete cascade
        on update cascade
);

create table library_documents
(
    library_id  bigint,
    document_id bigint,
    foreign key (library_id) references library (id)
        on delete cascade
        on update cascade,
    foreign key (document_id) references documents (id)
        on delete cascade
        on update cascade
);

create table user_library_owners
(
    user_id    bigint,
    library_id bigint,
    foreign key (user_id) references university_user (id)
        on delete cascade
        on update cascade,
    foreign key (library_id) references library (id)
        on delete cascade
        on update cascade
);

create table user_library_subscribers
(
    user_id    bigint,
    library_id bigint,
    foreign key (user_id) references university_user (id)
        on delete cascade
        on update cascade,
    foreign key (library_id) references library (id)
        on delete cascade
        on update cascade
);
