CREATE TABLE IF NOT EXISTS banners
(
  banner_id   BIGSERIAL PRIMARY KEY,
  title CHARACTER VARYING(255) NOT NULL,
  description TEXT,
  url CHARACTER VARYING(512) NOT NULL,
  flight_id BIGINT,
  file BYTEA,
  UNIQUE (title),
  CONSTRAINT flight_fkey FOREIGN KEY (flight_id)
    REFERENCES flights (flight_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
OIDS = FALSE
);
ALTER TABLE banners
  OWNER TO postgres;