
CREATE TABLE "amazonsalesrank" (
    azsrid bigint IDENTITY NOT NULL PRIMARY KEY,
	azsr_vpid bigint NOT NULL,
	azsr_primary_rank int NULL,
	azsr_primary_rank_category varchar(25),
	azsr_secondary_rank int,
	azsr_secondary_rank_category varchar(25),
	azsr_downloaded datetime,
	azsr_added_by int NOT NULL,
	azsr_added_dt datetime NOT NULL DEFAULT NOW(),
	azsr_lastupdated_by int,
	azsr_lastupdated_dt datetime
);

