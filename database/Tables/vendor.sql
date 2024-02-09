
CREATE TABLE [dbo].[vendor](
	[vdid] [int] IDENTITY(1,1) NOT NULL,
	[vd_active] bit NOT NULL default 1,
	[vd_name] [varchar](500) NOT NULL,
	[vd_website] [varchar](250) NULL,
	[vd_added_by] [int] NOT NULL,
	[vd_added_dt] [datetime] NOT NULL,
	[vd_lastupdated_by] [int] NULL,
	[vd_lastupdated_dt] [datetime] NULL,
CONSTRAINT [PK_vendor_vdid] PRIMARY KEY CLUSTERED 
( [vdid] ASC ) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
