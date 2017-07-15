CREATE TABLE t_user (
  id           SERIAL PRIMARY KEY,
  username     VARCHAR(20) UNIQUE,
  salt         VARCHAR(32),
  pwd          VARCHAR(64),
  uuid         VARCHAR(32)       NOT NULL,
  avator       INTEGER DEFAULT 0 NOT NULL,
  phone        VARCHAR(11) UNIQUE,
  email_status INTEGER DEFAULT 0 NOT NULL,
  email        TEXT DEFAULT '' :: TEXT UNIQUE
);
