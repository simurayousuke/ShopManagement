create table t_log
(
	id serial not null
		constraint t_log_pkey
			primary key,
	user_id integer not null,
	operation varchar(30) not null,
	happen_time timestamp(3) default now() not null,
	ip varchar(39),
	join_id integer,
	description text
)
;

