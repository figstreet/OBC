
CREATE TABLE "vendoraddress" (
	vdaid int IDENTITY NOT NULL PRIMARY KEY,
	vda_vdid int NOT NULL,
	vda_active bit NOT NULL default 1,
	vda_primary bit NOT NULL default 0,
	vda_type varchar_ignorecase(25) NOT NULL,
	vda_addr1 varchar(150),
	vda_addr2 varchar(150),
	vda_city varchar(50),
	vda_region char(25),
	vda_zip varchar(12),
	vda_country varchar_ignorecase(25) NOT NULL,
	vda_added_by int NOT NULL,
	vda_added_dt datetime NOT NULL,
	vda_lastupdated_by int,
	vda_lastupdated_dt datetime DEFAULT NOW()
);
