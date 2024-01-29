create table USERS
(
    ID         INTEGER identity
        constraint USERS_PK
            primary key,
    USERNAME   VARCHAR(64)  not null
        constraint USERS_UK
            unique,
    PASSWORD   VARCHAR(512) not null,
    ENABLED    BOOLEAN      not null,
    CREATED_AT TIMESTAMP    not null,
    UPDATED_AT TIMESTAMP    not null
);