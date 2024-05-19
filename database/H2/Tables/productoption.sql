
CREATE TABLE "productoption" (
    proid int IDENTITY NOT NULL PRIMARY KEY,
	pro_active bit NOT NULL default 1,
	pro_prid int NOT NULL,
	pro_similar_prid int NOT NULL,
	pro_optionpricediff numeric(20,2),
    pro_price_currency varchar_ignorecase(25),
	pro_added_by int NOT NULL,
	pro_added_dt datetime NOT NULL DEFAULT NOW(),
	pro_lastupdated_by int,
	pro_lastupdated_dt datetime
);

