
CREATE TABLE "userpermission" (
    upid int IDENTITY NOT NULL PRIMARY KEY,
	up_usid int NOT NULL,
	up_type varchar_ignorecase(25) NOT NULL,
	up_active bit NOT NULL default 1,
	up_added_by int NOT NULL,
	up_added_dt datetime NOT NULL DEFAULT NOW(),
	up_lastupdated_by int,
	up_lastupdated_dt datetime
);

