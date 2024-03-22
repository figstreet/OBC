
CREATE TABLE "productrating" (
    prrid bigint IDENTITY NOT NULL PRIMARY KEY,
	prr_prid int NOT NULL,
	prr_usid int NOT NULL,
	prr_rating numeric(10,2),
	prr_notes varchar(200),
	prr_added_by int NOT NULL,
	prr_added_dt datetime NOT NULL DEFAULT NOW(),
	prr_lastupdated_by int,
	prr_lastupdated_dt datetime
);

