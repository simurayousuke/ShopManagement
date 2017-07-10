create table t_good
(
	id serial not null
		constraint t_good_pkey
			primary key,
	good_name text not null,
	uuid varchar(32) not null,
	shop_id integer not null,
	description text default ''::text not null,
	avator integer default 0 not null,
	sale_count integer default 0 not null
)
;

