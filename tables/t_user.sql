CREATE TABLE t_user (
  id       SERIAL PRIMARY KEY,
  username VARCHAR(20)       NOT NULL UNIQUE,
  salt     VARCHAR(32)       NOT NULL,
  pwd      VARCHAR(64)       NOT NULL,
  uuid     VARCHAR(32)       NOT NULL,
  avator   INTEGER DEFAULT 0 NOT NULL,
  phone    VARCHAR(11)
);
