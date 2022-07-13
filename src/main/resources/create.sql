-- CREATE DATABASE site_maintenance;
-- CREATE TABLE engineers (id serial PRIMARY KEY, first_name VARCHAR, last_name VARCHAR, eng_no VARCHAR UNIQUE, phone_no VARCHAR UNIQUE, email VARCHAR UNIQUE, created TIMESTAMP, updated TIMESTAMP, deleted VARCHAR DEFAULT 'FALSE');
-- CREATE TABLE sites (id serial PRIMARY KEY, name VARCHAR UNIQUE, description varchar, engineer_id INTEGER, created TIMESTAMP, updated TIMESTAMP, deleted VARCHAR DEFAULT 'FALSE');
-- CREATE DATABASE site_maintenance_test WITH TEMPLATE site_maintenance;
select * from sites;
