
CREATE TABLE [dbo].[users](
	[usid] [int] IDENTITY(1,1) NOT NULL,
	[us_email] [varchar](100) NOT NULL, --login
	[us_display_id] [varchar](20) NOT NULL, --should be unique
	[us_active] bit NOT NULL default 1,
	[us_fname] [varchar](50) NOT NULL,
	[us_mname] [varchar](50),
	[us_lname] [varchar](50),
	[us_password] [varchar](60),
	[us_password_salt] [varchar](60),
	[us_password_set_dt] [datetime],
	[us_confirmation_key] varchar(25),
	[us_confirmed] [datetime],
	[us_login_dt] [datetime],
	[us_login_failures] [int],
	[us_inactive_reason] [varchar](25),
	[us_added_by] [int] NOT NULL,
	[us_added_dt] [datetime] NOT NULL,
	[us_lastupdated_by] [int],
	[us_lastupdated_dt] [datetime],
CONSTRAINT [PK_users_usid] PRIMARY KEY CLUSTERED 
( [usid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
