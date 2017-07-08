CREATE TABLE t_email (
  id            SERIAL PRIMARY KEY,
  user_id       INTEGER           NOT NULL,
  email         TEXT UNIQUE,
  status        INTEGER DEFAULT 0 NOT NULL,
  activity_code VARCHAR(32)       NOT NULL,
  binding_time  TIMESTAMP(3)
);
