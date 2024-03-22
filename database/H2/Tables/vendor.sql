
CREATE TABLE "vendor" (
	vdid int IDENTITY NOT NULL PRIMARY KEY,
	vd_active bit NOT NULL default 1,
	vd_name varchar(500) NOT NULL,
	vd_website varchar(250),
	vd_added_by int NOT NULL,
	vd_added_dt datetime NOT NULL DEFAULT NOW(),
	vd_lastupdated_by int,
	vd_lastupdated_dt datetime
);

