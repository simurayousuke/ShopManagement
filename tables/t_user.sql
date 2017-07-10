create table t_user
(
	id serial not null
		constraint t_user_pkey
			primary key,
	username varchar(20),
	salt varchar(32),
	pwd varchar(64),
	uuid varchar(32) not null,
	avator integer default 0 not null,
	phone varchar(11),
	email_status integer default 0 not null,
	email text default ''::text
)
;

create unique index t_user_username_uindex
	on t_user (username)
;

create unique index t_user_phone_uindex
	on t_user (phone)
;

create unique index t_user_email_uindex
	on t_user (email)
;

