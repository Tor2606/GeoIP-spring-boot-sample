ALTER TABLE users
  ADD COLUMN role CHARACTER VARYING(255),
  OWNER TO postgres;


INSERT INTO users (email, password, first_name, last_name, user_ip, role) VALUES
  ('admin@admin.com', '$2a$10$p3XI00RfZe2r3wisRXcede0LSupqEqg6XRFqgb7ogcqHs8.A4GjeC', 'admin', 'administratovich', '',
   'ROLE_ADMIN');