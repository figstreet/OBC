
CREATE TABLE "vendor_product" (
	vpid bigint IDENTITY NOT NULL PRIMARY KEY,
	vp_vdid int NOT NULL,
	vp_prid int NOT NULL,
	vp_active bit NOT NULL default 1,
	vp_price numeric(20,2),
	vp_quantity int,
	vp_alternative_price numeric(20,2),
	vp_min_order_quantity int,
	vp_available_online bit NULL default 1,
	vp_vendor_identifier varchar(30),
	vp_amazon_marketplace varchar_ignorecase(25),
	vp_price_currency varchar_ignorecase(25),
	vp_downloaded datetime,
	vp_added_by int NOT NULL,
	vp_added_dt datetime NOT NULL,
	vp_lastupdated_by int,
	vp_lastupdated_dt datetime DEFAULT NOW()
);

