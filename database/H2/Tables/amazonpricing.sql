
CREATE TABLE "amazonpricing" (
    azpid bigint IDENTITY NOT NULL PRIMARY KEY,
	azp_vpid bigint NOT NULL,
	azp_product_condition varchar(25),
	azp_offer_count smallint NOT NULL,
	azp_price_currency varchar(25),
	azp_buybox_item_price numeric(20,2),
	azp_buybox_shipping_price numeric(20,2),
	azp_buybox_fba bit NOT NULL default 0,
	azp_buybox_seller_amazon bit NOT NULL default 0,
	azp_secondary_item_price numeric(20,2),
	azp_secondary_shipping_price numeric(20,2),
	azp_secondary_fba bit NOT NULL default 0,
	azp_downloaded datetime,
	azp_added_by int NOT NULL,
	azp_added_dt datetime NOT NULL DEFAULT NOW(),
	azp_lastupdated_by int,
	azp_lastupdated_dt datetime
);

