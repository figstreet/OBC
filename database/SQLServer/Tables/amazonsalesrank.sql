
CREATE TABLE [dbo].[amazonsalesrank](
	[azsrid] [bigint] IDENTITY(1,1) NOT NULL,
	[azsr_vpid] [bigint] NOT NULL,
	[azsr_primary_rank] [int] NULL,
	[azsr_primary_rank_category] varchar(25) NULL,
	[azsr_secondary_rank] [int] NULL,
	[azsr_secondary_rank_category] varchar(25) NULL,
	[azsr_downloaded] [datetime],
	[azsr_added_by] [int] NOT NULL,
	[azsr_added_dt] [datetime] NOT NULL,
	[azsr_lastupdated_by] [int] NULL,
	[azsr_lastupdated_dt] [datetime] NULL,
CONSTRAINT [PK_amazonsalesrank_azsrid] PRIMARY KEY CLUSTERED 
( [azsrid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
