
CREATE TABLE "history" (
    hiid int IDENTITY NOT NULL PRIMARY KEY,
	hi_prid int,
	hi_vdid int,
	hi_vdaid int,
	hi_vdcid int,
	hi_type varchar_ignorecase(25) NOT NULL,
	hi_prior_value varchar(4000),
	hi_azpid bigint,
	hi_azsrid bigint,
	hi_changed_dt datetime NOT NULL,
	hi_added_by int NOT NULL,
	hi_added_dt datetime NOT NULL DEFAULT NOW()
);

