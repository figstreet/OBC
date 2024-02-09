
CREATE TABLE [dbo].[vendor_product](
	[vpid] [bigint] IDENTITY(1,1) NOT NULL,
	[vp_vdid] [int] NOT NULL,
	[vp_prid] [int] NOT NULL,
	[vp_active] bit NOT NULL default 1,
	[vp_price] [float] NULL,
	[vp_quantity] [int] NULL,
	[vp_alternative_price] [float] NULL,
	[vp_min_order_quantity] [int] NULL,
	[vp_available_online] bit NULL default 1,
	[vp_vendor_identifier] [varchar](30) NULL, 
	[vp_amazon_marketplace] [varchar](25) NULL,
	[vp_price_currency] [varchar] [25] NULL,
	[vp_downloaded] [datetime] NULL,
	[vp_added_by] [int] NOT NULL,
	[vp_added_dt] [datetime] NOT NULL,
	[vp_lastupdated_by] [int] NULL,
	[vp_lastupdated_dt] [datetime] NULL,
CONSTRAINT [PK_vendor_product] PRIMARY KEY CLUSTERED 
( [vpid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
