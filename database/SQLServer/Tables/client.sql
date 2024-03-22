
CREATE TABLE [dbo].[client] (
    [clid] int NOT NULL,
    [cl_name] varchar(200) NOT NULL,
    [cl_active] [bit] NOT NULL default 1,
    [cl_added_by] [int] NOT NULL,
    [cl_added_dt] [datetime] NOT NULL DEFAULT GETDATE(),
    [cl_lastupdated_by] [int] NULL,
    [cl_lastupdated_dt] [datetime] NULL,
    CONSTRAINT [PK_client] PRIMARY KEY CLUSTERED
        ( [clid] ASC )
        WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY];

GO

insert into client (clid, cl_name, cl_active, cl_added_by, cl_lastupdated_by, cl_lastupdated_dt)
values (0, 'GENERAL', 1, 100, 100, GETDATE());

GO
