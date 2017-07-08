CREATE TABLE t_file (
  id        SERIAL PRIMARY KEY,
  url       TEXT                     NOT NULL,
  file_name TEXT                     NOT NULL,
  type      VARCHAR(10) DEFAULT ''   NOT NULL,
  size      BIGINT DEFAULT (-1)      NOT NULL
);
