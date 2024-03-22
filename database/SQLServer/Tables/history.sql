
CREATE TABLE [dbo].[history](
	[hiid] int IDENTITY(1,1) NOT NULL,
	[hi_prid] [int],
	[hi_vdid] [int],
	[hi_vdaid] [int],
	[hi_vdcid] [int],
	[hi_type] [varchar](25) NOT NULL,
	[hi_prior_value]  [varchar](2000),
	[hi_azpid] [bigint],
	[hi_azsrid] [bigint],
	[hi_changed_dt] [datetime] NOT NULL,
	[hi_added_by] [int] NOT NULL,
	[hi_added_dt] [datetime] NOT NULL,
CONSTRAINT [PK_history_hiid] PRIMARY KEY CLUSTERED 
( 	[hiid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

