DROP TABLE IF EXISTS user_data;
DROP TABLE IF EXISTS user_requests;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS persistent_logins;

CREATE TABLE IF NOT EXISTS companies
(
  company_id BIGSERIAL PRIMARY KEY,
  name       CHARACTER VARYING(255) NOT NULL,
  UNIQUE (name)
)
WITH (
OIDS = FALSE
);
ALTER TABLE companies
  OWNER TO postgres;

CREATE TABLE IF NOT EXISTS users
(
  user_id    BIGSERIAL PRIMARY KEY,
  email      CHARACTER VARYING(255) NOT NULL,
  password   CHARACTER VARYING(255)  NOT NULL,
  first_name CHARACTER VARYING(30),
  last_name  CHARACTER VARYING(100),
  user_ip    CHARACTER VARYING(45),
  company_id BIGINT,
  role CHARACTER VARYING(255),
  UNIQUE (email),
  CONSTRAINT companies_fkey FOREIGN KEY (company_id)
  REFERENCES companies (company_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
OIDS = FALSE
);
ALTER TABLE users
  OWNER TO postgres;

CREATE INDEX user_email_index
ON users (email);

CREATE TABLE IF NOT EXISTS user_data (
  user_data_id BIGSERIAL PRIMARY KEY,
  time TIMESTAMP,
  user_id BIGINT,
  device_category CHARACTER VARYING(100),
  ip CHARACTER VARYING(45),
  country CHARACTER VARYING(10),
  user_agent CHARACTER VARYING(500),
  browser CHARACTER VARYING(100),
  operating_system CHARACTER VARYING(100),
  agent_family CHARACTER VARYING(100),
  producer CHARACTER VARYING(100),
  CONSTRAINT user_fkey FOREIGN KEY (user_id)
  REFERENCES users (user_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE user_data
  OWNER TO postgres;

CREATE TABLE IF NOT EXISTS user_requests (
  request_id BIGSERIAL PRIMARY KEY,
  time TIMESTAMP,
  user_id BIGINT,
  ip CHARACTER VARYING(45),
  url CHARACTER VARYING(300),
  CONSTRAINT user_fkey FOREIGN KEY (user_id)
  REFERENCES users (user_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE user_requests
  OWNER TO postgres;

CREATE TABLE IF NOT EXISTS persistent_logins
(
  username  CHARACTER VARYING(64)       NOT NULL,
  series    CHARACTER VARYING(64)       NOT NULL,
  token     CHARACTER VARYING(64)       NOT NULL,
  last_used TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT persistent_logins_pkey PRIMARY KEY (series)
)
WITH (
OIDS = FALSE
);
ALTER TABLE persistent_logins
  OWNER TO postgres;
INSERT INTO companies(name) VALUES ('test_company');
INSERT INTO users(email, password, first_name, last_name, user_ip, role, company_id) VALUES ('admin@admin.com','$2a$10$p3XI00RfZe2r3wisRXcede0LSupqEqg6XRFqgb7ogcqHs8.A4GjeC','admin','administratovich','','ROLE_ADMIN', 1);