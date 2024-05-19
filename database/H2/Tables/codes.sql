
CREATE TABLE "codes" (
    coid int IDENTITY NOT NULL PRIMARY KEY,
	co_active bit NOT NULL default 1,
	co_type varchar_ignorecase(10) NOT NULL,
	co_value varchar_ignorecase(25) NOT NULL,
	co_desc varchar(100),
	co_added_by int NOT NULL,
	co_added_dt datetime NOT NULL DEFAULT NOW(),
	co_lastupdated_by int,
	co_lastupdated_dt datetime
);

