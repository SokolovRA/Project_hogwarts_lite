--liquibase formatted sql
--changeset romansokolov:1
create table student_avatar (
      id BIGSERIAL NOT NULL PRIMARY KEY ,
      data BYTEA,
      file_path VARCHAR(255) NOT NULL,
      file_size BIGINT,
      media_type VARCHAR(255),
      student_id BIGINT NOT NULL REFERENCES student(id));

