CREATE TABLE IF NOT EXISTS campaigns
(
  campaign_id BIGSERIAL PRIMARY KEY,
  name        CHARACTER VARYING(255) NOT NULL,
  created TIMESTAMP,
  user_id     BIGINT,
  UNIQUE (name),
  CONSTRAINT user_fkey FOREIGN KEY (user_id)
  REFERENCES users (user_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
OIDS = FALSE
);
ALTER TABLE campaigns
  OWNER TO postgres;

CREATE TABLE IF NOT EXISTS websites
(
  website_id BIGSERIAL PRIMARY KEY,
  name       CHARACTER VARYING(255) NOT NULL,
  UNIQUE (name)
)
WITH (
OIDS = FALSE
);
ALTER TABLE websites
  OWNER TO postgres;

CREATE TABLE IF NOT EXISTS flights
(
  flight_id   BIGSERIAL PRIMARY KEY,
  name        CHARACTER VARYING(255) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE,
  campaign_id BIGINT,
  UNIQUE (name),
  CONSTRAINT campaign_fkey FOREIGN KEY (campaign_id)
  REFERENCES campaigns (campaign_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
OIDS = FALSE
);
ALTER TABLE flights
  OWNER TO postgres;

CREATE TABLE IF NOT EXISTS websites_flights (
  website_id  INT NOT NULL,
  flight_id INT NOT NULL,
  PRIMARY KEY (website_id, flight_id),

  CONSTRAINT website_fkey FOREIGN KEY (website_id)
  REFERENCES websites (website_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,

  CONSTRAINT flight_fkey FOREIGN KEY (flight_id)
  REFERENCES flights (flight_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE websites_flights
  OWNER TO postgres;

CREATE INDEX website_id_index
ON websites_flights (website_id);

CREATE INDEX campaign_id_index
ON websites_flights (flight_id);
