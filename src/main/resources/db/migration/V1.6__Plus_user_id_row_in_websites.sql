ALTER TABLE websites
  ADD COLUMN user_id BIGINT,
  DROP CONSTRAINT websites_website_name_key,
  ADD CONSTRAINT user_fkey FOREIGN KEY (user_id)
REFERENCES users (user_id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE CASCADE,
  OWNER TO postgres;

ALTER TABLE flights
  DROP CONSTRAINT website_fkey,
  ADD CONSTRAINT website_fkey FOREIGN KEY (website_id)
REFERENCES websites (website_id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE RESTRICT,
  OWNER TO postgres;