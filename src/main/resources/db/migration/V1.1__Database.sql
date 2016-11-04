CREATE TABLE IF NOT EXISTS users
(
  user_id    BIGSERIAL PRIMARY KEY,
  email      CHARACTER VARYING(100) NOT NULL,
  password   CHARACTER VARYING(255)  NOT NULL,
  first_name CHARACTER VARYING(30),
  last_name  CHARACTER VARYING(100),
  user_ip    CHARACTER VARYING(45),
  UNIQUE (email)
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
  ON UPDATE NO ACTION ON DELETE SET NULL
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
