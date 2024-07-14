create table if not exists USERS
(
    ID         INTEGER identity
        constraint USERS_PK
            primary key,
    USERNAME   VARCHAR(192)  not null
        constraint USERS_UK
            unique,
    PASSWORD   VARCHAR(512)  not null,
    ENABLED    BOOLEAN       not null,
    CREATED_AT TIMESTAMP     not null,
    UPDATED_AT TIMESTAMP     not null
);

create table if not exists ROLES
(
    ID          INTEGER identity
        constraint ROLES_PK
            primary key,
    NAME        VARCHAR(16) not null
        constraint ROLES_UK
            unique,
    DESCRIPTION VARCHAR(64),
    CREATED_AT  TIMESTAMP   not null,
    UPDATED_AT  TIMESTAMP   not null
);

create table if not exists USER_ROLES
(
    USER_ID INTEGER not null
        constraint USER_ROLES_USERS_ID_FK
            references USERS,
    ROLE_ID INTEGER not null
        constraint USER_ROLES_ROLES_ID_FK
            references ROLES,
    constraint USER_ROLES_PK
        primary key (USER_ID, ROLE_ID)
);
