--liquibase formatted sql
--changeset romansokolov:changes_table_student
ALTER TABLE student  ADD CONSTRAINT age_constraint CHECK (age > 16);

--changeset romansokolov:changes_table_faculty
ALTER TABLE faculty ADD CONSTRAINT name_unique UNIQUE (name);