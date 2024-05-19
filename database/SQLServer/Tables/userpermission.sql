
CREATE TABLE [dbo].[userpermission](
	[upid] [int] IDENTITY(1,1) NOT NULL,
	[up_usid] [int] NOT NULL,
	[up_type] varchar(25),
	[up_active] bit NOT NULL default 1,
	[up_added_by] [int] NOT NULL,
	[up_added_dt] [datetime] NOT NULL,
	[up_lastupdated_by] [int],
	[up_lastupdated_dt] [datetime],
CONSTRAINT [PK_userpermission_upid] PRIMARY KEY CLUSTERED 
( [upid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
