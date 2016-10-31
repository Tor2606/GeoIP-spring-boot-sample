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

ALTER TABLE users
  ADD COLUMN company_id BIGINT,
  ADD CONSTRAINT companies_fkey FOREIGN KEY (company_id)
REFERENCES companies (company_id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION,
  OWNER TO postgres;

INSERT INTO companies (name) VALUES ('test_company');
UPDATE users
SET company_id = 1
WHERE user_id = 1;