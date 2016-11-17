CREATE TABLE IF NOT EXISTS companies
(
  company_id   BIGSERIAL PRIMARY KEY,
  company_name CHARACTER VARYING(255) NOT NULL,
  UNIQUE (company_name)
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

INSERT INTO companies (company_name) VALUES ('test_company');
UPDATE users
SET company_id = 1
WHERE user_id = 1;