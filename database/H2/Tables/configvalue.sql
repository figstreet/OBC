
CREATE TABLE "configvalue" (
    cvid int IDENTITY NOT NULL PRIMARY KEY,
    cv_clid int NOT NULL default 0,
	cv_active bit NOT NULL default 1,
	cv_config_type varchar_ignorecase(25) NOT NULL,
	cv_property_name varchar_ignorecase(25) NOT NULL,
	cv_property_value varchar_ignorecase(2000),
	cv_added_by int NOT NULL,
	cv_added_dt datetime NOT NULL DEFAULT NOW(),
	cv_lastupdated_by int,
	cv_lastupdated_dt datetime
);

