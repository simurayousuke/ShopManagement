CREATE TABLE t_shop (
  id            SERIAL PRIMARY KEY,
  shop_name     TEXT              NOT NULL UNIQUE,
  uuid          VARCHAR(32)       NOT NULL,
  description   TEXT DEFAULT '',
  owner_user_id INTEGER           NOT NULL,
  avator        INTEGER DEFAULT 0 NOT NULL
);
