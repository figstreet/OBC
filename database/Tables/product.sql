
CREATE TABLE [dbo].[product](
	[prid] [int] IDENTITY(1,1) NOT NULL,
	[pr_active] bit NOT NULL default 1,
	[pr_name] [varchar](100) NOT NULL,
	[pr_short_desc] [varchar](200) NULL,
	[pr_long_desc] [varchar](2000) NULL,
	[pr_upc] [varchar](20) NULL,
	[pr_sku] [varchar](20) NULL,
	[pr_length] [float] NULL,
	[pr_width] [float] NULL,
	[pr_height] [float] NULL,
	[pr_weight] [float] NULL,
	[pr_length_unit] [varchar](25) NULL,
	[pr_width_unit] [varchar](25) NULL,
	[pr_height_unit] [varchar](25) NULL,
	[pr_weight_unit] [varchar](25) NULL,
	[pr_image_url] [varchar](1000) NULL,
	[pr_list_price] [float] NULL,
	[pr_price_currency] [varchar](25) NULL,
	[pr_added_by] [int] NOT NULL,
	[pr_added_dt] [datetime] NOT NULL,
	[pr_lastupdated_by] [int] NULL,
	[pr_lastupdated_dt] [datetime] NULL,
 CONSTRAINT [PK_product_prid] PRIMARY KEY CLUSTERED 
( 	[prid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
