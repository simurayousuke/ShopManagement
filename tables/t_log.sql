CREATE TABLE t_log (
  id          SERIAL PRIMARY KEY,
  user_id     INTEGER                    NOT NULL,
  operation   VARCHAR(30)                NOT NULL,
  happen_time TIMESTAMP(3) DEFAULT now() NOT NULL,
  ip          VARCHAR(39),
  join_id     INTEGER,
  description TEXT
);

