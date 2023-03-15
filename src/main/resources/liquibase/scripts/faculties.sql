--liquibase formatted sql
--changeset romansokolov:create_table_faculty
create table faculty (
    id BIGSERIAL NOT NULL PRIMARY KEY ,
    name VARCHAR(100) NOT NULL,
    color VARCHAR(100) NOT NULL);

