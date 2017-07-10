create table t_shop
(
	id serial not null
		constraint t_shop_pkey
			primary key,
	shop_name text not null,
	uuid varchar(32) not null,
	description text default ''::text,
	owner_user_id integer not null,
	avator integer default 0 not null
)
;

create unique index t_shop_shop_name_uindex
	on t_shop (shop_name)
;

