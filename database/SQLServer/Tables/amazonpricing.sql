
CREATE TABLE [dbo].[amazonpricing](
	[azpid] [bigint] IDENTITY(1,1) NOT NULL,
	[azp_vpid] [bigint] NOT NULL,
	[azp_product_condition] varchar(25) NULL,
	[azp_offer_count] [smallint] NOT NULL,
	[azp_price_currency] varchar(25) NULL,
	[azp_buybox_item_price] [float] NULL,
	[azp_buybox_shipping_price] [float] NULL,
	[azp_buybox_fba] bit NOT NULL default 0,
	[azp_buybox_seller_amazon] bit NOT NULL default 0,
	[azp_secondary_item_price] [float] NULL,
	[azp_secondary_shipping_price] [float] NULL,
	[azp_secondary_fba] bit NOT NULL default 0,
	[azp_downloaded] [datetime],
	[azp_added_by] [int] NOT NULL,
	[azp_added_dt] [datetime] NOT NULL,
	[azp_lastupdated_by] [int] NULL,
	[azp_lastupdated_dt] [datetime] NULL,
CONSTRAINT [PK_amazonpricing_azpid] PRIMARY KEY CLUSTERED 
( [azpid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
