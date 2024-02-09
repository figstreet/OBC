
CREATE TABLE [dbo].[productoption](
	[proid] [int] IDENTITY(1,1) NOT NULL,
	[pro_active] [bit] NOT NULL default 1,
	[pro_prid] [int] NOT NULL,
	[pro_similar_prid] int NOT NULL,
	[pro_optionpricediff] [float] NULL,
	[pro_added_by] [int] NOT NULL,
	[pro_added_dt] [datetime] NOT NULL,
	[pro_lastupdated_by] [int] NULL,
	[pro_lastupdated_dt] [datetime] NULL,
 CONSTRAINT [PK_productoption_proid] PRIMARY KEY CLUSTERED 
( [proid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
