
CREATE TABLE [dbo].[productrating](
	[prrid] [bigint] IDENTITY(1,1) NOT NULL,
	[prr_prid] [int] NOT NULL,
	[prr_usid] [int] NOT NULL,
	[prr_rating] [float] NULL,
	[prr_notes] [varchar](200) NULL,
	[prr_added_by] [int] NOT NULL,
	[prr_added_dt] [datetime] NOT NULL,
	[prr_lastupdated_by] [int] NULL,
	[prr_lastupdated_dt] [datetime] NULL,
CONSTRAINT [PK_productrating_prrid] PRIMARY KEY CLUSTERED 
( 	[prrid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]