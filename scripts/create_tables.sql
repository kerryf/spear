create table if not exists users
(
    id         integer not null
        constraint users_pk
            primary key,
    username   text    not null,
    password   text    not null,
    first_name text    not null,
    last_name  text,
    enabled    integer not null,
    created_at text    not null,
    updated_at text    not null
);

create table if not exists roles
(
    id         integer not null
        constraint roles_pk
            primary key,
    name       text    not null
        constraint roles_uk
            unique,
    created_at text    not null,
    updated_at text    not null
);

create table if not exists user_role
(
    user_id integer not null
        constraint user_role_users_id_fk
            references users,
    role_id integer not null
        constraint user_role_roles_id_fk
            references roles,
    constraint user_role_pk
        primary key (user_id, role_id)
);
