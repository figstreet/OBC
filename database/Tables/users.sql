
CREATE TABLE [dbo].[users](
	[usid] [int] IDENTITY(101,1) NOT NULL,
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
	[us_added_dt] [datetime] NOT NULL DEFAULT GETDATE(),
	[us_lastupdated_by] [int],
	[us_lastupdated_dt] [datetime],
CONSTRAINT [PK_users_usid] PRIMARY KEY CLUSTERED 
( [usid] ASC )
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

set identity_insert [users] on;

insert into [users] (usid, us_email, us_display_id, us_active, us_fname, us_lname
                  ,us_added_by, us_lastupdated_by, us_lastupdated_dt)
values (100, 'admin@figstreet.com', 'ADMIN', 0, 'System', 'Admin', 100, 100, GETDATE());

set identity_insert [users] off;

GO
