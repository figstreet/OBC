
CREATE TABLE "users" (
    usid int IDENTITY (START WITH 101) NOT NULL PRIMARY KEY,
	us_email varchar_ignorecase(100) NOT NULL, --login
	us_display_id varchar_ignorecase(20) NOT NULL, --should be unique
	us_active bit NOT NULL default 1,
	us_fname varchar_ignorecase(50) NOT NULL,
	us_mname varchar_ignorecase(50),
	us_lname varchar_ignorecase(50),
	us_password varchar(60),
	us_password_salt varchar(60),
	us_password_set_dt datetime,
	us_confirmation_key varchar(25),
	us_confirmed datetime,
	us_login_dt datetime,
	us_login_failures int,
	us_inactive_reason varchar_ignorecase(25),
	us_added_by int NOT NULL,
	us_added_dt datetime NOT NULL DEFAULT NOW(),
	us_lastupdated_by int,
	us_lastupdated_dt datetime
);


insert into users (usid, us_email, us_display_id, us_active, us_fname, us_lname
                  ,us_added_by, us_lastupdated_by, us_lastupdated_dt)
values (100, 'admin@figstreet.com', 'ADMIN', 0, 'System', 'Admin', 100, 100, NOW());

