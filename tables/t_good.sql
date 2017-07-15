CREATE TABLE t_good (
  id          SERIAL PRIMARY KEY,
  good_name   TEXT              NOT NULL,
  uuid        VARCHAR(32)       NOT NULL,
  shop_id     INTEGER           NOT NULL,
  description TEXT DEFAULT ''   NOT NULL,
  avator      INTEGER DEFAULT 0 NOT NULL,
  sale_count  INTEGER DEFAULT 0 NOT NULL
);
