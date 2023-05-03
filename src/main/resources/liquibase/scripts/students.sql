--liquibase formatted sql
--changeset romansokolov:create_table_student
create table student(
    id         BIGSERIAL       NOT NULL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    age        INTEGER NOT NULL,
    faculty_id BIGINT           REFERENCES faculty (id));






