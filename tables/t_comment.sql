create table t_comment
(
	id serial not null
		constraint t_comment_pkey
			primary key,
	good_id integer not null,
	buyer_id integer not null,
	is_good integer default 1 not null,
	description text default 'Ä¬ÈÏºÃÆÀ'::text not null,
	shop_id integer not null
)
;

