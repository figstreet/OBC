
CREATE TABLE [dbo].[codes](
	[coid] int IDENTITY(1,1) NOT NULL,
	[co_active] [bit] NOT NULL default 1,
	[co_type] [varchar](10) NOT NULL,
	[co_value] [varchar](25) NOT NULL,
	[co_desc]  [varchar](100),
	[co_added_by] [int] NOT NULL,
	[co_added_dt] [datetime] NOT NULL,
	[co_lastupdated_by] [int] NULL,
	[co_lastupdated_dt] [datetime] NULL,
CONSTRAINT [PK_codes_coid] PRIMARY KEY CLUSTERED 
( 	[coid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]