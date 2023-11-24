create table IF NOT EXISTS "user"
(
    id       integer generated always as identity
        constraint user_id
            primary key,
    login    varchar,
    password varchar,
    key      varchar
);

-- alter table "user"
--     owner to postgres;
--
