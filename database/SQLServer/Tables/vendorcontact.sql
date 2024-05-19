
CREATE TABLE [dbo].[vendorcontact](
	[vdcid] [int] IDENTITY(1,1) NOT NULL,
	[vdc_vdid] [int] NOT NULL,
	[vdc_active] bit NOT NULL default 1,
	[vdc_primary] bit NOT NULL default 0,
	[vdc_type] varchar(25) NOT NULL,
	[vdc_email] varchar(100),
	[vdc_country_code] varchar(6),
	[vdc_phone1] [char](3) NULL,
	[vdc_phone2] [char](3) NULL,
	[vdc_phone3] [char](4) NULL,
	[vdc_phone_ext] [varchar](10) NULL,
	[vdc_contact_name1] varchar(100),
	[vdc_contact_name2] varchar(100),
	[vdc_added_by] [int] NOT NULL,
	[vdc_added_dt] [datetime] NOT NULL,
	[vdc_lastupdated_by] [int] NULL,
	[vdc_lastupdated_dt] [datetime] NULL,
CONSTRAINT [PK_vendorcontact_vdcid] PRIMARY KEY CLUSTERED 
( [vdcid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
