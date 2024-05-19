
CREATE TABLE "product" (
    prid int IDENTITY NOT NULL PRIMARY KEY,
	pr_active bit NOT NULL default 1,
	pr_name varchar_ignorecase(100) NOT NULL,
	pr_short_desc varchar(200),
	pr_long_desc varchar(2000),
	pr_upc varchar_ignorecase(20),
	pr_sku varchar_ignorecase(20),
	pr_length numeric(20,4),
	pr_width numeric(20,4),
	pr_height numeric(20,4),
	pr_weight numeric(20,4),
	pr_length_unit varchar_ignorecase(25),
	pr_width_unit varchar_ignorecase(25),
	pr_height_unit varchar_ignorecase(25),
	pr_weight_unit varchar_ignorecase(25),
	pr_image_url varchar_ignorecase(1000),
	pr_list_price numeric(20,2),
	pr_price_currency varchar_ignorecase(25),
	pr_added_by int NOT NULL,
	pr_added_dt datetime NOT NULL DEFAULT NOW(),
	pr_lastupdated_by int,
	pr_lastupdated_dt datetime
);

