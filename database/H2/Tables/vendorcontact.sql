
CREATE TABLE "vendorcontact" (
	vdcid int IDENTITY NOT NULL PRIMARY KEY,
	vdc_vdid int NOT NULL,
	vdc_active bit NOT NULL default 1,
	vdc_primary bit NOT NULL default 0,
	vdc_type varchar_ignorecase(25) NOT NULL,
	vdc_email varchar_ignorecase(100),
	vdc_country_code varchar(6),
	vdc_phone1 char(3),
	vdc_phone2 char(3),
	vdc_phone3 char(4),
	vdc_phone_ext varchar(10),
	vdc_contact_name1 varchar(100),
	vdc_contact_name2 varchar(100),
	vdc_added_by int NOT NULL,
	vdc_added_dt datetime NOT NULL,
	vdc_lastupdated_by int,
	vdc_lastupdated_dt datetime DEFAULT NOW()
);
