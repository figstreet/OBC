
CREATE TABLE [dbo].[configvalue](
	[cvid] int IDENTITY(1,1) NOT NULL,
	[cv_active] [bit] NOT NULL default 1,
	[cv_config_type] [varchar](25) NOT NULL,
	[cv_property_name] [varchar](25) NOT NULL,
	[cv_property_value]  [varchar](2000),
	[cv_added_by] [int] NOT NULL,
	[cv_added_dt] [datetime] NOT NULL,
	[cv_lastupdated_by] [int] NULL,
	[cv_lastupdated_dt] [datetime] NULL,
CONSTRAINT [PK_configvalue_cvid] PRIMARY KEY CLUSTERED 
( 	[cvid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]