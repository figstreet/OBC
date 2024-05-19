
CREATE TABLE [dbo].[vendoraddress](
	[vdaid] [int] IDENTITY(1,1) NOT NULL,
	[vda_vdid] [int] NULL,
	[vda_active] bit NOT NULL default 1,
	[vda_primary] bit NOT NULL default 0,
	[vda_type] [varchar](25) NOT NULL,
	[vda_addr1] [varchar](150) NULL,
	[vda_addr2] [varchar](150) NULL,
	[vda_city] [varchar](50) NULL,
	[vda_region] [char](25) NULL,
	[vda_zip] [varchar](12) NULL,
	[vda_country] [varchar](25) NOT NULL,
	[vda_added_by] [int] NOT NULL,
	[vda_added_dt] [datetime] NOT NULL,
	[vda_lastupdated_by] [int] NULL,
	[vda_lastupdated_dt] [datetime] NULL,
CONSTRAINT [PK_vendoraddress_vdaid] PRIMARY KEY CLUSTERED 
( [vdaid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
