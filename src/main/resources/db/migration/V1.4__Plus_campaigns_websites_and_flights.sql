CREATE TABLE IF NOT EXISTS campaigns
(
  campaign_id   BIGSERIAL PRIMARY KEY,
  campaign_name CHARACTER VARYING(255) NOT NULL,
  created       TIMESTAMP,
  user_id       BIGINT,
  UNIQUE (campaign_name),
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
  website_id   BIGSERIAL PRIMARY KEY,
  website_name CHARACTER VARYING(255) NOT NULL,
  UNIQUE (website_name)
)
WITH (
OIDS = FALSE
);
ALTER TABLE websites
  OWNER TO postgres;

CREATE TABLE IF NOT EXISTS flights
(
  flight_id   BIGSERIAL PRIMARY KEY,
  flight_name CHARACTER VARYING(255) NOT NULL,
  start_date  DATE,
  end_date    DATE,
  type CHARACTER VARYING(255),
  campaign_id BIGINT,
  website_id BIGINT,
  quantity INT,
  UNIQUE (flight_name),
  CONSTRAINT campaign_fkey FOREIGN KEY (campaign_id)
  REFERENCES campaigns (campaign_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT website_fkey FOREIGN KEY (website_id)
  REFERENCES websites (website_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
);
ALTER TABLE flights
  OWNER TO postgres;