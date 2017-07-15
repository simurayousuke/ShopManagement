CREATE TABLE t_comment (
  id          SERIAL PRIMARY KEY,
  good_id     INTEGER             NOT NULL,
  buyer_id    INTEGER             NOT NULL,
  is_good     INTEGER DEFAULT 1   NOT NULL,
  description TEXT DEFAULT '默认好评' NOT NULL,
  shop_id     INTEGER             NOT NULL
);
